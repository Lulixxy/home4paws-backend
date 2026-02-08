package org.itsci.home4paws.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
public class HandoverRequest {
    private LocalDate handoverDate;
    private String handoverPerson;
    private String remarks;
}
