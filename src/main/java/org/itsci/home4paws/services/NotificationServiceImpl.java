package org.itsci.home4paws.services;

import jakarta.transaction.Transactional;
import org.itsci.home4paws.model.Notification;
import org.itsci.home4paws.model.User;
import org.itsci.home4paws.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private NotificationRepository nr;

    @Override
    public List<Notification> getMyNotifications(String username) {
        return nr.findByMemberUsername(username);
    }

    @Override
    @Transactional
    public void createNotification(User receiver, String title, String message, String icon) {
        Notification noti = new Notification();

        String latestId = nr.findMaxNotificationId();
        String newId;
        if (latestId == null) {
            newId = "N00001";
        } else {
            int num = Integer.parseInt(latestId.substring(1));
            num++;
            newId = String.format("N%05d", num);
        }
        noti.setNotificationId(newId);

        noti.setBriefMessage(title);
        noti.setMessage(message);
        noti.setIcon(icon);
        noti.setMember(receiver);
        noti.setCreateDate(new Date());
        noti.setRead(false);

        nr.save(noti);
    }
}
