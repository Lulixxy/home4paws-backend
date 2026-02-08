package org.itsci.home4paws.services;

import org.itsci.home4paws.DTO.AdoptRequestDTO;
import org.itsci.home4paws.DTO.AdoptRequestResponse;
import org.itsci.home4paws.model.AdoptRequest;

import java.util.List;

public interface AdoptRequestService {
    List<AdoptRequestResponse> getRequestsByPosterId(String memberId);
    AdoptRequestResponse getRequestById(String requestId);
    AdoptRequest approveRequest(String requestId);
    void makeRequest(AdoptRequestDTO dto);

}
