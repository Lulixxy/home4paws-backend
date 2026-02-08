package org.itsci.home4paws.services;

import jakarta.transaction.Transactional;
import org.itsci.home4paws.DTO.ReviewRequest;
import org.itsci.home4paws.model.Review;
import org.itsci.home4paws.model.User;
import org.itsci.home4paws.model.Wellbeing;
import org.itsci.home4paws.repository.ReviewRepository;
import org.itsci.home4paws.repository.WellbeingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    private ReviewRepository rr;

    @Autowired
    private WellbeingRepository wr;

    @Autowired
    private NotificationService notiSer;

    //เพิ่มการรีวิวการเลี้ยงสัตว์
    @Override
    @Transactional
    public Review addReview(ReviewRequest req) {
        Review review = new Review();

        String latestId = rr.findMaxReviewId();
        String newId;
        if (latestId == null) {
            newId = "R00001";
        } else {
            int num = Integer.parseInt(latestId.substring(1));
            num++;
            newId = String.format("R%05d", num);
        }
        review.setReviewId(newId);

        review.setRating(req.getRating());
        review.setComment(req.getComment());

        Wellbeing wellbeing = wr.findById(req.getWellbeingId())
                .orElseThrow(() -> new RuntimeException("Wellbeing not found with ID: " + req.getWellbeingId()));
        review.setWellbeing(wellbeing);

        Review savedReview = rr.save(review);

        // TRIGGER 4: เมื่อ Owner กดรีวิว -> แจ้งเตือน Adopter
        // แจ้งเตือน Adopter
        try {
            // ต้องหาตัว Adopter จากความสัมพันธ์: Review -> Wellbeing -> Adoption -> Adopter
            User adopter = wellbeing.getAdoption().getAdoptRequest().getAdopter();

            notiSer.createNotification(
                    adopter,
                    "คุณได้รับรีวิวใหม่ ⭐",
                    "เจ้าของเดิมให้คะแนนการเลี้ยงดูของคุณ " + req.getRating() + " ดาว",
                    "star"
            );
        } catch (Exception e) {
            System.out.println("Error sending review notification: " + e.getMessage());
        }

        return savedReview;
    }

    @Override
    public Optional<Review> getReviewById(String reviewId) {
        return rr.findById(reviewId);
    }
}
