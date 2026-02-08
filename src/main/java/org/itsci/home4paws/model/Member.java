package org.itsci.home4paws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "member")
@PrimaryKeyJoinColumn(name = "member_id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Member extends User {

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "age", length = 3)
    private String age;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "sub_district", length = 100)
    private String subDistrict;

    @Column(name = "postal_code", length = 5)
    private String postalCode;

    @Column(name = "address_type", length = 20)
    private String addressType;

    @Column(name = "income")
    private Double income;

    @OneToMany(mappedBy = "adopter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<AdoptRequest> adoptRequests;

    public Member() {
    }

    public Member(String firstName, String lastName, String gender, String age, Date dateOfBirth, String province, String district, String subDistrict, String postalCode, String addressType, Double income, List<AdoptRequest> adoptRequests) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.province = province;
        this.district = district;
        this.subDistrict = subDistrict;
        this.postalCode = postalCode;
        this.addressType = addressType;
        this.income = income;
        this.adoptRequests = adoptRequests;
    }

    public Member(String userId, String profilePicture, String username, String password, String email, String phoneNumber, String memberType, String address, Boolean banStatus, List<Notification> notifications, List<PostAnimal> animals, String firstName, String lastName, String gender, String age, Date dateOfBirth, String province, String district, String subDistrict, String postalCode, String addressType, Double income, List<AdoptRequest> adoptRequests) {
        super(userId, profilePicture, username, password, email, phoneNumber, memberType, address, banStatus, notifications, animals);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.province = province;
        this.district = district;
        this.subDistrict = subDistrict;
        this.postalCode = postalCode;
        this.addressType = addressType;
        this.income = income;
        this.adoptRequests = adoptRequests;
    }

}
