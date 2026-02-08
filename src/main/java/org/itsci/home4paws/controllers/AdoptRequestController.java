package org.itsci.home4paws.controllers;

import org.itsci.home4paws.DTO.AdoptRequestDTO;
import org.itsci.home4paws.DTO.AdoptRequestResponse;
import org.itsci.home4paws.model.AdoptRequest;
import org.itsci.home4paws.services.AdoptRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@CrossOrigin(origins = "*")
public class AdoptRequestController {

    @Autowired
    private AdoptRequestService ars;

    // ดึงคำขอที่เข้ามา (สำหรับเจ้าของโพสต์ดู)
    @GetMapping("/incoming")
    public ResponseEntity<List<AdoptRequestResponse>> getIncomingRequests(@RequestParam("username") String username) {
        List<AdoptRequestResponse> requestList = ars.getRequestsByPosterId(username);
        return ResponseEntity.ok(requestList);
    }

    // ดึงรายละเอียดคำขอตาม ID
    @GetMapping("/{id}")
    public ResponseEntity<AdoptRequestResponse> getRequestById(@PathVariable("id") String id) {
        try {
            AdoptRequestResponse response = ars.getRequestById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // อนุมัติคำขอ
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable("id") String requestId) {
        try {
            AdoptRequest approved = ars.approveRequest(requestId);
            // ส่งกลับเป็น DTO เพื่อความปลอดภัยและสวยงาม
            return ResponseEntity.ok(new AdoptRequestResponse(approved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // สร้างคำขอรับเลี้ยง
    @PostMapping("/make-request")
    public ResponseEntity<String> doMakeRequest(@RequestBody AdoptRequestDTO dto) {
        try {
            ars.makeRequest(dto);
            return ResponseEntity.ok("Request submitted successfully.");
        } catch (RuntimeException e) {
            // ส่ง Error กลับไปให้หน้าบ้านรู้ (เช่น ขอซ้ำ, หา User ไม่เจอ)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}