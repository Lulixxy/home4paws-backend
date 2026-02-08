package org.itsci.home4paws.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "post_animal")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PostAnimal {
    @Id
    @Column(name = "animal_id", length = 10)
    private String animalId;

    @Lob // บอกว่าเป็น Large Object (ข้อมูลขนาดใหญ่)
    @Column(name = "animal_image", columnDefinition = "LONGTEXT")
    private String animalImage;

    @Column(name = "animal_name", length = 50)
    private String animalName;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "breed", length = 100)
    private String breed;

    @Column(name = "age", length = 2)
    private Integer age;

    @Column(name = "personality", length = 150)
    private String personality;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "animal_status", length = 10)
    private String animalStatus;

    @Column(name = "animal_type", length = 5)
    private String animalType;

    @Column(name = "appropriate")
    private Boolean appropriate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private User member;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<AdoptRequest> adoptRequests;

    public PostAnimal() {
    }

    public PostAnimal(String animalId, String animalImage, String animalName, String gender, String breed, Integer age, String personality, String location, String animalStatus, String animalType, Boolean appropriate, User member, List<AdoptRequest> adoptRequests) {
        this.animalId = animalId;
        this.animalImage = animalImage;
        this.animalName = animalName;
        this.gender = gender;
        this.breed = breed;
        this.age = age;
        this.personality = personality;
        this.location = location;
        this.animalStatus = animalStatus;
        this.animalType = animalType;
        this.appropriate = appropriate;
        this.member = member;
        this.adoptRequests = adoptRequests;
    }

    // เพื่อส่ง username ไปให้หน้าบ้าน (Frontend) field 'member' ถูกซ่อนด้วย @JsonBackReference
    @JsonGetter("username")
    public String getUsername() {
        return member != null ? member.getUsername() : "";
    }
}
