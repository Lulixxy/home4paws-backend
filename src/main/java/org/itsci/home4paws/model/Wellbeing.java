package org.itsci.home4paws.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "wellbeing")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Wellbeing {
    @Id
    @Column(name = "wellbeing_id", length = 10, unique = true)
    private String wellbeingId;

    @Column(name = "images", columnDefinition = "LONGTEXT")
    private String images;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "adoption_id", nullable = false)
    @JsonBackReference
    private Adoption adoption;

    @OneToOne(mappedBy = "wellbeing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Review review;

    public Wellbeing() {
    }

    public Wellbeing(String wellbeingId, String images, String description, Date updateDate, Adoption adoption, Review review) {
        this.wellbeingId = wellbeingId;
        this.images = images;
        this.description = description;
        this.updateDate = updateDate;
        this.adoption = adoption;
        this.review = review;
    }
}
