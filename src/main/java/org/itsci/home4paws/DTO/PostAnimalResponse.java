package org.itsci.home4paws.DTO;

import lombok.Getter;
import lombok.Setter;
import org.itsci.home4paws.model.PostAnimal;

@Getter
@Setter
public class PostAnimalResponse {
    private String animalId;
    private String animalImage;
    private String animalName;
    private String animalType;
    private String location;
    private String gender;
    private String breed;
    private int age;
    private String personality;
    private String animalStatus;
    private String username;

    public PostAnimalResponse(PostAnimal pa) {
        this.animalId = pa.getAnimalId();
        this.animalImage = pa.getAnimalImage();
        this.animalName = pa.getAnimalName();
        this.animalType = pa.getAnimalType();
        this.location = pa.getLocation();
        this.gender = pa.getGender();
        this.breed = pa.getBreed();
        this.age = pa.getAge();
        this.personality = pa.getPersonality();
        this.animalStatus = pa.getAnimalStatus();
        if (pa.getMember() != null) {
            this.username = pa.getMember().getUsername();
        } else {
            this.username = "";
        }
    }
}
