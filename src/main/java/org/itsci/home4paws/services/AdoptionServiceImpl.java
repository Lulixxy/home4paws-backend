package org.itsci.home4paws.services;

import jakarta.transaction.Transactional;
import org.itsci.home4paws.DTO.AdoptionResponse;
import org.itsci.home4paws.model.AdoptRequest;
import org.itsci.home4paws.model.Adoption;
import org.itsci.home4paws.repository.AdoptionRequestRepository;
import org.itsci.home4paws.repository.AdoptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdoptionServiceImpl implements AdoptionService {

    @Autowired
    private AdoptionRepository ar;

    @Autowired
    private AdoptionRequestRepository reqRepo;

    // ดึงรายการรับเลี้ยงจากสัตว์ที่เราโพสต์ไว้ (Owner ดู)
    @Override
    public List<AdoptionResponse> getAdoptionsByOwnerId(String userId) {
        List<Adoption> adoptions = ar.findByAnimalOwner(userId);
        return adoptions.stream()
                .map(adoption -> new AdoptionResponse(adoption))
                .collect(Collectors.toList());
    }

    // ดึงรายการที่เราเป็นคนขอรับเลี้ยงแล้วถูกอนุมัติ (Adopter ดู - เดิม)
    @Override
    public List<AdoptionResponse> getAdoptionsByAdopterId(String userId) {
        List<Adoption> adoptions = ar.findByAdopter(userId);
        return adoptions.stream()
                .map(adoption -> new AdoptionResponse(adoption))
                .collect(Collectors.toList());
    }

    // อัปเดตการส่งมอบ
    @Transactional
    @Override
    public Adoption updateHandoverDetails(String adoptionId, LocalDate handoverDate, String handoverPerson, String remarks) {
        Adoption adoption = ar.findById(adoptionId)
                .orElseThrow(() -> new RuntimeException("Adoption not found with ID: " + adoptionId));

        Date date = Date.from(handoverDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        adoption.setHandoverDate(date);
        adoption.setHandoverPerson(handoverPerson);
        adoption.setRemarks(remarks);

        return ar.save(adoption);
    }

    // เพิ่มใหม่!! ดึงประวัติทั้งหมด (Pending, Approved, Rejected)
    @Override
    public List<AdoptionResponse> getMyAdoptionHistory(String username) {
        // เรียก Repository (ตรวจสอบชื่อ method ให้ตรงกับใน AdoptRequestRepository นะคะ)
        List<AdoptRequest> requests = reqRepo.findByAdopterUsername(username);

        List<AdoptionResponse> responses = new ArrayList<>();

        for (AdoptRequest req : requests) {
            AdoptionResponse dto = new AdoptionResponse();

            if (req.getAnimal() != null) {
                dto.setAnimalName(req.getAnimal().getAnimalName());
                dto.setAnimalImage(req.getAnimal().getAnimalImage());
                dto.setAnimalId(req.getAnimal().getAnimalId());
            }

            dto.setRequestStatus(req.getRequestStatus());
            dto.setRequestDate(req.getRequestDate());

            // Logic หา Adoption ID
            if ("Approved".equalsIgnoreCase(req.getRequestStatus()) || "Completed".equalsIgnoreCase(req.getRequestStatus())) {
                Adoption adoption = ar.findByAdoptRequestRequestId(req.getRequestId());
                if (adoption != null) {
                    dto.setAdoptionId(adoption.getAdoptionId());
                    // ✅ 4. แปลง Date -> String
                    if (adoption.getHandoverDate() != null) {
                        dto.setHandoverDate(adoption.getHandoverDate().toString());
                    }
                }
            }
            responses.add(dto);
        }
        return responses;
    }
}