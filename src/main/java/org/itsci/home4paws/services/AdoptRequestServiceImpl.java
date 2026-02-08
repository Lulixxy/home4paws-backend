package org.itsci.home4paws.services;

import jakarta.transaction.Transactional;
import org.itsci.home4paws.DTO.AdoptRequestDTO;
import org.itsci.home4paws.DTO.AdoptRequestResponse;
import org.itsci.home4paws.model.AdoptRequest;
import org.itsci.home4paws.model.Adoption;
import org.itsci.home4paws.model.Member;
import org.itsci.home4paws.model.PostAnimal;
import org.itsci.home4paws.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdoptRequestServiceImpl implements AdoptRequestService {
    @Autowired
    private AdoptionRequestRepository arr;
    @Autowired
    private AdoptionRepository ar;
    @Autowired
    private PostAnimalRepository pr;
    @Autowired
    private MemberRepository mr;
    @Autowired
    private NotificationService notiSer;

    // ดึงคำขอที่สถานะ "Pending" (รออนุมัติ) ของเจ้าของโพสต์
    @Override
    public List<AdoptRequestResponse> getRequestsByPosterId(String username) {
        List<AdoptRequest> requests = arr.findByAnimalPoster(username);
        return requests.stream()
                .map(AdoptRequestResponse::new)
                .collect(Collectors.toList());
    }

    // ดึงคำขอตาม ID
    @Override
    public AdoptRequestResponse getRequestById(String requestId) {
        AdoptRequest request = arr.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with ID: " + requestId));
        return new AdoptRequestResponse(request);
    }

    // อนุมัติคำขอรับเลี้ยง แล้วปฏิเสธคำขออื่นอัตโนมัติ
    @Transactional
    @Override
    public AdoptRequest approveRequest(String requestId) {
        AdoptRequest approvedRequest = arr.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // 1. เปลี่ยนสถานะเป็น "Approved"
        approvedRequest.setRequestStatus("Approved");

        // 2. สร้างข้อมูล Adoption (การรับเลี้ยงสำเร็จ)
        Adoption adoption = new Adoption();
        String latestId = ar.findMaxAdoptionId();
        String newId = (latestId == null) ? "A00001" : String.format("A%05d", Integer.parseInt(latestId.substring(1)) + 1);

        adoption.setAdoptionId(newId);
        adoption.setAdoptRequest(approvedRequest);
        approvedRequest.setAdoption(adoption);

        ar.save(adoption);

        // TRIGGER 2: แจ้งเตือนคนขอว่า "อนุมัติแล้ว" (Adopter)
        try {
            notiSer.createNotification(
                    approvedRequest.getAdopter(),
                    "คำขอได้รับการอนุมัติ! 🎉",
                    "ยินดีด้วย! คุณได้รับการอนุมัติให้รับเลี้ยงน้อง " + approvedRequest.getAnimal().getAnimalName(),
                    "approved"
            );
        } catch (Exception e) {
            System.out.println("Error notifying approval: " + e.getMessage());
        }

        // 3. จัดการปฏิเสธคนอื่น (Reject others)
        PostAnimal animal = approvedRequest.getAnimal();
        List<AdoptRequest> allRequests = animal.getAdoptRequests();

        for (AdoptRequest req : allRequests) {
            // ถ้าไม่ใช่ request ที่เราเพิ่งกดอนุมัติ ให้ Reject ให้หมด
            if (!req.getRequestId().equals(requestId)) {
                req.setRequestStatus("Rejected");
                arr.save(req);

                // TRIGGER 3: แจ้งเตือนคนอื่นว่า "เสียใจด้วย" (Rejected)
                try {
                    notiSer.createNotification(
                            req.getAdopter(),
                            "คำขอไม่ผ่านการพิจารณา ❌",
                            "เสียใจด้วย คำขอรับเลี้ยงน้อง " + animal.getAnimalName() + " ไม่ได้รับการอนุมัติ",
                            "rejected"
                    );
                } catch (Exception e) {
                    System.out.println("Error notifying rejection: " + e.getMessage());
                }
            }
        }

        // 4. อัปเดตสถานะสัตว์
        animal.setAnimalStatus("Adopted");
        pr.save(animal);

        return approvedRequest;
    }

    // สร้างคำขอรับเลี้ยง (User กดส่งมา)
    @Override
    public void makeRequest(AdoptRequestDTO dto) {
        // แปลง Username -> Member
        Member adopter = mr.findByUsername(dto.getUsername());
        if (adopter == null) {
            throw new RuntimeException("Adopter not found: " + dto.getUsername());
        }

        PostAnimal animal = pr.findById(dto.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Animal not found"));

        // เช็คว่าเคยขอหรือยัง
        boolean alreadyRequested = arr.existsByAdopterAndAnimal(adopter.getUsername(), animal.getAnimalId());
        if (alreadyRequested) {
            throw new RuntimeException("You have already submitted a request for this animal.");
        }

        AdoptRequest request = new AdoptRequest();

        // สร้าง ID
        String latestId = arr.findMaxRequestId();
        String newId = (latestId == null) ? "R00001" : String.format("R%05d", Integer.parseInt(latestId.substring(1)) + 1);

        request.setRequestId(newId);
        request.setAdopter(adopter);
        request.setAnimal(animal);
        request.setRequestDate(new Date());
        request.setRequestStatus("Pending");

        AdoptRequest savedReq = arr.save(request);

        // TRIGGER 1: แจ้งเตือน Owner ว่า "มีคนขอเลี้ยง"
        try {
            // เช็คว่า User ใน PostAnimal ชื่อ getOwner() หรือ getMember() ให้แก้ตามจริงนะคะ
            // ในที่นี้สมมติว่าเป็น getOwner() ตามโค้ดเดิมของหนู
            notiSer.createNotification(
                    savedReq.getAnimal().getMember(),
                    "มีคำขอรับเลี้ยงใหม่! 🐾",
                    "คุณ " + savedReq.getAdopter().getUsername() + " สนใจน้อง " + savedReq.getAnimal().getAnimalName(),
                    "new_request"
            );
        } catch (Exception e) {
            System.out.println("Error sending notification: " + e.getMessage());
        }
    }
}