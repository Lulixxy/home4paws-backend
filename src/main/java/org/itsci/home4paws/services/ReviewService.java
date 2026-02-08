package org.itsci.home4paws.services;

import org.itsci.home4paws.DTO.ReviewRequest;
import org.itsci.home4paws.model.Review;

import java.util.Optional;

public interface ReviewService {
    Review addReview(ReviewRequest request);

    Optional<Review> getReviewById(String reviewId);
}
