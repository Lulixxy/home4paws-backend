package org.itsci.home4paws.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "review")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Review {
    @Id
    @Column(name = "review_id", length = 10, unique = true)
    private String reviewId;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "comment", length = 500)
    private String comment;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "wellbeing_id", unique = true)
    @JsonBackReference
    private Wellbeing wellbeing;

    public Review() {
    }

    public Review(String reviewId, Double rating, String comment, Wellbeing wellbeing) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.comment = comment;
        this.wellbeing = wellbeing;
    }

}
