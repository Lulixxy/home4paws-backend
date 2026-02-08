package org.itsci.home4paws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @Column(name = "user_id", length = 10, unique = true, nullable = false)
    private String userId;

    @Lob // บอกว่าเป็น Large Object (ข้อมูลขนาดใหญ่)
    @Column(name = "profile_picture", columnDefinition = "LONGTEXT") // บังคับให้เป็น LONGTEXT
    private String profilePicture;

    @Column(name = "username", unique = true, length = 12, nullable = false)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "email", unique = true, length = 60, nullable = false)
    private String email;

    @Column(name = "phone_number", unique = true, length = 10, nullable = false)
    private String phoneNumber;

    @Column(name = "member_type", length = 10, nullable = false)
    private String memberType;

    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Column(name = "ban_status")
    private Boolean banStatus;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Notification> notifications;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PostAnimal> animals;

    public User() {
    }

    public User(String userId, String profilePicture, String username, String password, String email, String phoneNumber, String memberType, String address, Boolean banStatus, List<Notification> notifications, List<PostAnimal> animals) {
        this.userId = userId;
        this.profilePicture = profilePicture;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.memberType = memberType;
        this.address = address;
        this.banStatus = banStatus;
        this.notifications = notifications;
        this.animals = animals;
    }

}
