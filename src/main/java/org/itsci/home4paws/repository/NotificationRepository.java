package org.itsci.home4paws.repository;

import org.itsci.home4paws.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,String> {

    @Query("SELECT MAX(n.notificationId) FROM Notification n")
    String findMaxNotificationId();

    // ดึงการแจ้งเตือนทั้งหมดของ User (เอาล่าสุดขึ้นก่อน)
    @Query("SELECT n FROM Notification n WHERE n.member.username = :username ORDER BY n.notificationId DESC")
    List<Notification> findByMemberUsername(@Param("username") String username);

}
