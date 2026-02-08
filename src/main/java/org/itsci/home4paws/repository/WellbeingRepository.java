package org.itsci.home4paws.repository;

import org.itsci.home4paws.model.Wellbeing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WellbeingRepository extends JpaRepository<Wellbeing,String> {
    @Query("SELECT MAX(w.wellbeingId) FROM Wellbeing w")
    String findMaxWellbeingId();
    // ดึงไทม์ไลน์ทั้งหมดของ Adoption ID นี้ (เรียงจากล่าสุดไปเก่าสุด)
    @Query("SELECT w FROM Wellbeing w WHERE w.adoption.adoptionId = :adoptionId ORDER BY w.updateDate DESC")
    List<Wellbeing> findByAdoptionId(@Param("adoptionId") String adoptionId);
}
