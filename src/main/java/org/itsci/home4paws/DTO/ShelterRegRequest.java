package org.itsci.home4paws.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShelterRegRequest {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String memberType;
    private String address;
    private String profilePicture;

    // Shelter-specific fields
    private String shelterName;
    private String registrationNumber;

    public ShelterRegRequest() {
    }

}
