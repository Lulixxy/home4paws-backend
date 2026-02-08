package org.itsci.home4paws.controllers;

import org.itsci.home4paws.DTO.ReviewRequest;
import org.itsci.home4paws.model.Review;
import org.itsci.home4paws.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService rs;

    //เพิ่มการรีวิวการเลี้ยงสัตว์
    @PostMapping("/add")
    public ResponseEntity<Review> doAddReview(@RequestBody ReviewRequest request) {
        Review review = rs.addReview(request);
        return ResponseEntity.ok(review);
    }

    //ดูการให้คะแนน
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable("id") String id) {
        return rs.getReviewById(id)
                .map(review -> ResponseEntity.ok(review))
                .orElse(ResponseEntity.notFound().build());
    }
}
