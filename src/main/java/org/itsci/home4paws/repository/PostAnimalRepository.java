package org.itsci.home4paws.repository;

import org.itsci.home4paws.model.PostAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostAnimalRepository extends JpaRepository<PostAnimal,String> {

    @Query("SELECT MAX(p.animalId) FROM PostAnimal p")
    String findMaxAnimalId();

    @Query("SELECT p FROM PostAnimal p WHERE p.member.username = :username AND p.appropriate = true")
    List<PostAnimal> findByMemberUsername(@Param("username") String username);

    @Query("SELECT p FROM PostAnimal p WHERE p.animalStatus = 'Available' AND p.appropriate = true")
    List<PostAnimal> findAvailablePosts();

    @Query("SELECT p FROM PostAnimal p WHERE p.animalStatus = 'Available' AND p.appropriate = true AND (" +
            "LOWER(p.animalName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.animalType) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.breed) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<PostAnimal> searchPostsByKeyword(@Param("keyword") String keyword);

    // นับจำนวนสัตว์ที่ยังหาบ้านอยู่ (Available)
    long countByAnimalStatus(String status);

    // หาโพสต์ที่ไม่เหมาะสม (appropriate = false)
    List<PostAnimal> findByAppropriateFalse();

}
