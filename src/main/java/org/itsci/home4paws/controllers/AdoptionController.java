package org.itsci.home4paws.controllers;

import org.itsci.home4paws.DTO.AdoptionResponse;
import org.itsci.home4paws.DTO.HandoverRequest;
import org.itsci.home4paws.model.Adoption;
import org.itsci.home4paws.services.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {
    @Autowired
    private AdoptionService as;

    // ดึงรายการรับเลี้ยงจากสัตว์ที่เราโพสต์ไว้ (Owner ดู)
    @GetMapping("/from-my-posts")
    public ResponseEntity<List<AdoptionResponse>> getAdoptionsFromMyPosts(@RequestParam("username") String username) {
        List<AdoptionResponse> list = as.getAdoptionsByOwnerId(username);
        return ResponseEntity.ok(list);
    }

    // ดึงรายการที่เราเป็นคนขอรับเลี้ยงแล้วถูกอนุมัติ (Adopter ดู)
    @GetMapping("/my-adoptions")
    public ResponseEntity<List<AdoptionResponse>> getMyAdoptions(@RequestParam("username") String username) {
        List<AdoptionResponse> list = as.getAdoptionsByAdopterId(username);
        return ResponseEntity.ok(list);
    }

    // อัปเดตการส่งมอบ (Handover)
    @PutMapping("/{adoptionId}/handover")
    public ResponseEntity<?> updateHandover(
            @PathVariable String adoptionId,
            @RequestBody HandoverRequest req) {
        try {
            Adoption updated = as.updateHandoverDetails(
                    adoptionId,
                    req.getHandoverDate(),
                    req.getHandoverPerson(),
                    req.getRemarks()
            );
            return ResponseEntity.ok(updated); // หรือจะ return "Success" ก็ได้
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // เพิ่มใหม่!! ดึงประวัติทั้งหมด (Pending, Approved, Rejected)
    // แบบที่ 1: สำหรับ Flutter หน้า Home (ส่งมาแบบ /my-history/Lulix01)
    @GetMapping("/my-history/{username}")
    public ResponseEntity<?> getMyHistoryPath(@PathVariable("username") String username) {
        return getMyAdoptionHistory(username); // เรียกใช้ Logic เดียวกัน
    }

    // แบบที่ 2: สำหรับกรณีเผื่ออยากส่งแบบ ?username=Lulix01
    @GetMapping("/my-history")
    public ResponseEntity<?> getMyHistoryQuery(@RequestParam("username") String username) {
        return getMyAdoptionHistory(username);
    }

    // Logic กลาง
    private ResponseEntity<?> getMyAdoptionHistory(String username) {
        try {
            List<AdoptionResponse> history = as.getMyAdoptionHistory(username);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}