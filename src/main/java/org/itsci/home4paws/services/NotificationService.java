package org.itsci.home4paws.services;

import org.itsci.home4paws.model.Notification;
import org.itsci.home4paws.model.User;

import java.util.List;

public interface NotificationService {
    // ดึงรายการแจ้งเตือน
    List<Notification> getMyNotifications(String username);

    // สร้างการแจ้งเตือน (เรียกใช้จากภายในระบบ)
    void createNotification(User receiver, String title, String message, String icon);
}
