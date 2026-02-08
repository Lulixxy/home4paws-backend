package org.itsci.home4paws.services;

import org.itsci.home4paws.model.Adoption;
import org.itsci.home4paws.repository.AdoptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ScheduledTasksService {

    @Autowired
    private AdoptionRepository adoptionRepo; // ต้องใช้ Repo ของ Adoption

    @Autowired
    private NotificationService notiService;

    // ----------------------------------------------------------------
    // TRIGGER 5: ตรวจสอบทุกวัน เวลา 09:00 น.
    // ----------------------------------------------------------------
    @Scheduled(cron = "0 0 9 * * *")
    public void notifyUpdateWellbeing() {
        System.out.println("⏰ Checking for wellbeing updates...");

        // ดึงรายการรับเลี้ยงทั้งหมดออกมาเช็ค (ควรกรองเฉพาะสถานะ Completed ถ้าทำได้)
        List<Adoption> adoptions = adoptionRepo.findAll();
        LocalDate today = LocalDate.now();

        for (Adoption adoption : adoptions) {
            // ต้องมีการส่งมอบแล้วถึงจะเริ่มนับวัน
            if (adoption.getHandoverDate() != null) {
                // แปลง Date (Legacy) เป็น LocalDate (Modern)
                LocalDate handoverDate = adoption.getHandoverDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                // คำนวณจำนวนวันที่ผ่านไป
                long daysBetween = ChronoUnit.DAYS.between(handoverDate, today);

                // เช็คว่าหาร 90 ลงตัวไหม (90, 180, 270...) คือครบทุก 3 เดือน
                if (daysBetween > 0 && daysBetween % 90 == 0) {

                    // แจ้งเตือน Adopter
                    notiService.createNotification(
                            adoption.getAdoptRequest().getAdopter(),
                            "ครบกำหนดอัปเดตชีวิตน้อง 📸",
                            "น้อง " + adoption.getAdoptRequest().getAnimal().getAnimalName() + " เป็นไงบ้าง? ครบ 3 เดือนแล้ว มาโพสต์รูปอวดหน่อยเร็ว!",
                            "camera"
                    );

                    System.out.println(">> Notified user: " + adoption.getAdoptRequest().getAdopter().getUsername());
                }
            }
        }
    }
}