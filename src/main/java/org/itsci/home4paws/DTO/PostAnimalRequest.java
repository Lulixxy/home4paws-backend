package org.itsci.home4paws.DTO;

import lombok.Getter;
import lombok.Setter;
import org.itsci.home4paws.model.PostAnimal;

@Getter
@Setter
public class PostAnimalRequest {
    private String username;
    private String animalImage;
    private String animalName;
    private String gender;
    private String breed;
    private int age;
    private String personality;
    private String location;
    private String animalType;
    private Boolean appropriate;

    public PostAnimal toEntity() {
        PostAnimal pa = new PostAnimal();
        pa.setAnimalImage(this.animalImage);
        pa.setAnimalName(this.animalName);
        pa.setGender(this.gender);
        pa.setBreed(this.breed);
        pa.setAge(this.age);
        pa.setPersonality(this.personality);
        pa.setLocation(this.location);
        pa.setAnimalType(this.animalType);
        pa.setAppropriate(this.appropriate);
        return pa;
    }

}
