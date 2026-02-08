package org.itsci.home4paws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "shelter")
@PrimaryKeyJoinColumn(name = "shelter_id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shelter extends User {

    @Column(name = "shelter_name", length = 100)
    private String shelterName;

    @Column(name = "registration_number", length = 20)
    private String registrationNumber;

    public Shelter() {
    }

    public Shelter(String shelterName, String registrationNumber) {
        this.shelterName = shelterName;
        this.registrationNumber = registrationNumber;
    }

    public Shelter(String userId, String profilePicture, String username, String password, String email, String phoneNumber, String memberType, String address, Boolean banStatus, List<Notification> notifications, List<PostAnimal> animals, String shelterName, String registrationNumber) {
        super(userId, profilePicture, username, password, email, phoneNumber, memberType, address, banStatus, notifications, animals);
        this.shelterName = shelterName;
        this.registrationNumber = registrationNumber;
    }

}
