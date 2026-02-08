package org.itsci.home4paws.controllers;

import org.itsci.home4paws.model.Notification;
import org.itsci.home4paws.repository.NotificationRepository;
import org.itsci.home4paws.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService ns;
    @Autowired
    private NotificationRepository nr;

    @GetMapping("/my-list")
    public ResponseEntity<List<Notification>> getMyNotifications(@RequestParam("username") String username) {
        List<Notification> list = ns.getMyNotifications(username);
        return ResponseEntity.ok(list);
    }

    // API สำหรับกดอ่านแจ้งเตือน
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable("id") String notiId) {
        return nr.findById(notiId).map(noti -> {
            noti.setRead(true); // เปลี่ยนเป็นอ่านแล้ว
            nr.save(noti);
            return ResponseEntity.ok("Read success");
        }).orElse(ResponseEntity.notFound().build());
    }
}
