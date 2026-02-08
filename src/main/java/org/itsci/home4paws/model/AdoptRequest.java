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
@Table(name = "adopt_request")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdoptRequest {
    @Id
    @Column(name = "request_id", length = 10, unique = true)
    private String requestId;

    @Column(name = "request_status", length = 20)
    private String requestStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "request_date")
    private Date requestDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adopter_id", nullable = false)
    @JsonBackReference
    private Member adopter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    @JsonBackReference
    private PostAnimal animal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_id", unique = true)
    @JsonManagedReference
    private Adoption adoption;

    public AdoptRequest() {
    }

    public AdoptRequest(String requestId, String requestStatus, Date requestDate, Member adopter, PostAnimal animal, Adoption adoption) {
        this.requestId = requestId;
        this.requestStatus = requestStatus;
        this.requestDate = requestDate;
        this.adopter = adopter;
        this.animal = animal;
        this.adoption = adoption;
    }

}
