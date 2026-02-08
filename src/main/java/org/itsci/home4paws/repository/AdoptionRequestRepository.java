package org.itsci.home4paws.repository;

import org.itsci.home4paws.model.AdoptRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // ✅ อย่าลืม import อันนี้
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRequestRepository extends JpaRepository<AdoptRequest, String> {

    @Query("SELECT MAX(ar.requestId) FROM AdoptRequest ar")
    String findMaxRequestId();

    @Query("SELECT r FROM AdoptRequest r WHERE r.animal.member.username = :username AND r.requestStatus = 'Pending' " +
            "ORDER BY r.animal.animalName ASC, r.requestDate DESC")
    List<AdoptRequest> findByAnimalPoster(@Param("username") String username);

    // เช็คว่า User คนนี้ เคยขอสัตว์ตัวนี้ไปหรือยัง
    @Query("SELECT count(r) > 0 FROM AdoptRequest r WHERE r.adopter.username = :username AND r.animal.animalId = :animalId")
    boolean existsByAdopterAndAnimal(@Param("username") String username, @Param("animalId") String animalId);

    // หาคำขอตามชื่อ User
    List<AdoptRequest> findByAdopterUsername(String username);
}