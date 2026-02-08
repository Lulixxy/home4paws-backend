package org.itsci.home4paws.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class MemberRegRequest {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String memberType;
    private String address;
    private String profilePicture;

    // Member-specific fields
    private String firstName;
    private String lastName;
    private String gender;
    private String age;
    private Date dateOfBirth;
    private String province;
    private String district;
    private String subDistrict;
    private String postalCode;
    private String addressType;
    private Double income;

    public MemberRegRequest() {
    }

}
