package org.itsci.home4paws.services;

import org.itsci.home4paws.DTO.MemberRegRequest;
import org.itsci.home4paws.DTO.ShelterRegRequest;

public interface MemberService {
    String doLogin(String username, String password);
    String registerMember(MemberRegRequest request);
    String registerShelter(ShelterRegRequest request);
}
