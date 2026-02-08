package org.itsci.home4paws.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private Double rating;
    private String comment;
    private String wellbeingId;
}
