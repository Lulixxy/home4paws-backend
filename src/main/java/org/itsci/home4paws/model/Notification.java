package org.itsci.home4paws.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "notification")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Notification {
    @Id
    @Column(name = "notification_id", length = 10)
    private String notificationId;

    @Column(name = "icon", columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String icon;

    @Column(name = "message", columnDefinition = "VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String message;

    @Column(name = "brief_message", columnDefinition = "VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String briefMessage;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "is_read")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private User member;

    public Notification() {
    }

    public Notification(String notificationId, String icon, String message, String briefMessage, User member) {
        this.notificationId = notificationId;
        this.icon = icon;
        this.message = message;
        this.briefMessage = briefMessage;
        this.member = member;
        this.createDate = new Date();
        this.isRead = false;
    }
}