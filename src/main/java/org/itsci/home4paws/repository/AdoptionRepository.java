package org.itsci.home4paws.repository;

import org.itsci.home4paws.model.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdoptionRepository extends JpaRepository<Adoption,String> {
    @Query("SELECT MAX(a.adoptionId) FROM Adoption a")
    String findMaxAdoptionId();

    @Query("SELECT a FROM Adoption a WHERE a.adoptRequest.animal.member.username = :username")
    List<Adoption> findByAnimalOwner(@Param("username") String username);

    @Query("SELECT a FROM Adoption a WHERE a.adoptRequest.adopter.username = :username")
    List<Adoption> findByAdopter(@Param("username") String username);

    // หาการรับเลี้ยง จาก ID ของคำขอ (เพื่อเชื่อมโยงกัน)
    Adoption findByAdoptRequestRequestId(String requestId);
}
