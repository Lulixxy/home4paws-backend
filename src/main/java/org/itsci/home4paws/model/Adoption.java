package org.itsci.home4paws.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "adoption")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Adoption {
    @Id
    @Column(name = "adoption_id", length = 10, unique = true)
    private String adoptionId;

    @Column(name = "handover_date")
    private Date handoverDate;

    @Column(name = "handover_person", length = 50)
    private String handoverPerson;

    @Column(name = "remarks", columnDefinition = "LONGTEXT")
    private String remarks;

    @OneToOne(mappedBy = "adoption", cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    @JsonBackReference
    private AdoptRequest adoptRequest;

    @OneToMany(mappedBy = "adoption", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Wellbeing> wellbeings;

    public Adoption() {
    }

    public Adoption(List<Wellbeing> wellbeings, AdoptRequest adoptRequest, String remarks, String handoverPerson, Date handoverDate, String adoptionId) {
        this.wellbeings = wellbeings;
        this.adoptRequest = adoptRequest;
        this.remarks = remarks;
        this.handoverPerson = handoverPerson;
        this.handoverDate = handoverDate;
        this.adoptionId = adoptionId;
    }

}
