package org.itsci.home4paws.services;

import jakarta.transaction.Transactional;
import org.itsci.home4paws.DTO.WellbeingRequest;
import org.itsci.home4paws.model.Adoption;
import org.itsci.home4paws.model.Wellbeing;
import org.itsci.home4paws.repository.AdoptionRepository;
import org.itsci.home4paws.repository.WellbeingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WellbeingServiceImpl implements WellbeingService {

    @Autowired
    private WellbeingRepository wr;

    @Autowired
    private AdoptionRepository ar;

    // สร้างการอัปเดตชีวิตสัตว์
    @Transactional
    @Override
    public Wellbeing doAddWellbeing(WellbeingRequest request) {
        Wellbeing wellbeing = new Wellbeing();

        String latestId = wr.findMaxWellbeingId();
        String newId;
        if (latestId == null) {
            newId = "W00001";
        } else {
            // สมมติ ID เป็น W00001 ตัดตัวแรกออกเหลือเลข
            int num = Integer.parseInt(latestId.substring(1));
            num++;
            newId = String.format("W%05d", num);
        }
        wellbeing.setWellbeingId(newId);

        wellbeing.setImages(request.getImages());
        wellbeing.setDescription(request.getDescription());

        wellbeing.setUpdateDate(new Date());

        Adoption adoption = ar.findById(request.getAdoptionId())
                .orElseThrow(() -> new RuntimeException("Adoption not found with ID: " + request.getAdoptionId()));
        wellbeing.setAdoption(adoption);

        return wr.save(wellbeing);
    }

    // ดูข้อมูลโพสต์เดียว
    @Override
    public Wellbeing getWellbeingById(String wellbeingId) {
        return wr.findById(wellbeingId)
                .orElseThrow(() -> new RuntimeException("Wellbeing not found with ID: " + wellbeingId));
    }

    // เพิ่มเมธอดนี้!! ดู Timeline ทั้งหมดของการรับเลี้ยง
    @Override
    public List<Wellbeing> getWellbeingByAdoptionId(String adoptionId) {
        // เรียก Repository ที่เราสร้าง Query ไว้ (เรียงตามวันที่)
        return wr.findByAdoptionId(adoptionId);
    }
}