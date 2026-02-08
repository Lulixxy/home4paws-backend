package org.itsci.home4paws.DTO;

import lombok.Getter;
import lombok.Setter;
import org.itsci.home4paws.model.AdoptRequest;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Getter
@Setter
public class AdoptRequestResponse {
    private String requestId;
    private String requestStatus;
    private String requestDate;

    // ข้อมูลคนขอ
    private String adopterImage;
    private String adopterName;
    private String adopterPhone;
    private String adopterAddress;
    private String adopterAddType;
    private String adopterEmail;
    private String adopterPassword;
    private Double adopterIncome;
    private String adopterAge;

    // ข้อมูลสัตว์
    private String animalId;
    private String animalName;
    private String animalImage;

    public AdoptRequestResponse(AdoptRequest request) {
        this.requestId = request.getRequestId();
        this.requestStatus = request.getRequestStatus();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH);
        this.requestDate = request.getRequestDate() != null ? sdf.format(request.getRequestDate()) : "-";

        // ✅ ดึงข้อมูลคนขอ
        if (request.getAdopter() != null) {
            this.adopterImage = request.getAdopter().getProfilePicture();
            this.adopterName = request.getAdopter().getUsername();
            this.adopterPhone = request.getAdopter().getPhoneNumber();
            this.adopterAddress = request.getAdopter().getProvince();
            this.adopterAddType = request.getAdopter().getAddressType();
            this.adopterIncome = request.getAdopter().getIncome();
            this.adopterAge = request.getAdopter().getAge();
        }

        // ✅ ดึงข้อมูลสัตว์
        if (request.getAnimal() != null) {
            this.animalId = request.getAnimal().getAnimalId();
            this.animalName = request.getAnimal().getAnimalName();
            this.animalImage = request.getAnimal().getAnimalImage();
        }
    }
}