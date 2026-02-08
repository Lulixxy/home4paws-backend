package org.itsci.home4paws.services;

import jakarta.transaction.Transactional;
import org.itsci.home4paws.DTO.AdoptRequestResponse;
import org.itsci.home4paws.DTO.AdoptionResponse;
import org.itsci.home4paws.model.Adoption;

import java.util.List;
import java.time.LocalDate;

public interface AdoptionService {
    List<AdoptionResponse> getAdoptionsByOwnerId(String userId);
    List<AdoptionResponse> getAdoptionsByAdopterId(String userId);
    Adoption updateHandoverDetails(String adoptionId, LocalDate handoverDate, String handoverPerson, String remarks);

    // เพิ่มใหม่!! ดึงประวัติทั้งหมด (Pending, Approved, Rejected)
    List<AdoptionResponse> getMyAdoptionHistory(String username);
}
