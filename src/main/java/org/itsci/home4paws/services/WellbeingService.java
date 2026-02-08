package org.itsci.home4paws.services;

import jakarta.transaction.Transactional;
import org.itsci.home4paws.DTO.WellbeingRequest;
import org.itsci.home4paws.model.Wellbeing;

import java.util.List;

public interface WellbeingService {
    @Transactional
    Wellbeing doAddWellbeing(WellbeingRequest request);

    Wellbeing getWellbeingById(String wellbeingId);

    List<Wellbeing> getWellbeingByAdoptionId(String adoptionId);
}
