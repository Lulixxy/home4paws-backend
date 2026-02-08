package org.itsci.home4paws.DTO;

import lombok.Getter;
import lombok.Setter;
import org.itsci.home4paws.model.AdoptRequest;
import org.itsci.home4paws.model.Adoption;

import java.util.Date;

@Getter
@Setter
public class AdoptionResponse {
    private String adoptionId;
    private String requestId;
    private String adopterName;
    private String animalName;
    private String animalImage;
    private String handoverDate;
    private String handoverPerson;
    private String remarks;
    private String requestStatus;
    private Date requestDate;
    private String animalId;

    public AdoptionResponse() {
    }

    public AdoptionResponse(Adoption adoption) {
        this.adoptionId = adoption.getAdoptionId();

        if (adoption.getHandoverDate() != null) {
            this.handoverDate = adoption.getHandoverDate().toString();
        } else {
            this.handoverDate = null;
        }
        this.handoverPerson = adoption.getHandoverPerson();
        this.remarks = adoption.getRemarks();

        AdoptRequest req = adoption.getAdoptRequest();
        if (req != null) {
            this.requestId = req.getRequestId();

            if (req.getAdopter() != null) {
                this.adopterName = req.getAdopter().getUsername();
            } else {
                this.adopterName = "-";
            }

            if (req.getAnimal() != null) {
                this.animalName = req.getAnimal().getAnimalName();
                this.animalImage = req.getAnimal().getAnimalImage();
                this.animalId = req.getAnimal().getAnimalId();
            } else {
                this.animalName = "Unknown";
            }
        }
    }
}