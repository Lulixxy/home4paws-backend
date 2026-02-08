package org.itsci.home4paws.repository;

import org.itsci.home4paws.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,String> {
    @Query("SELECT MAX(r.reviewId) FROM Review r")
    String findMaxReviewId();

    // หา Review ที่ rating น้อยกว่าค่าที่ส่งไป
    List<Review> findByRatingLessThan(Double rating);
}
