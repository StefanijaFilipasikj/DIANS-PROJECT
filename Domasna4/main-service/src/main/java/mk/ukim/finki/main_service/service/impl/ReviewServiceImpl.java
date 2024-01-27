package mk.ukim.finki.main_service.service.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.main_service.model.HistoricLandmark;
import mk.ukim.finki.main_service.model.Review;
import mk.ukim.finki.main_service.model.User;
import mk.ukim.finki.main_service.model.exception.InvalidReviewIdException;
import mk.ukim.finki.main_service.repository.ReviewRepository;
import mk.ukim.finki.main_service.service.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    @Override
    @Transactional
    public Review addNewReview(HistoricLandmark landmark, User user, String comment, Double rating) {
        Review review = new Review(user, comment, rating);
        reviewRepository.save(review);
        return review;
    }

    @Override
    public void editReviewById(Long id, Double rating, String comment) {
        Review review = this.reviewRepository.findById(id).orElseThrow(InvalidReviewIdException::new);
        review.setComment(comment);
        review.setRating(rating);
        this.reviewRepository.save(review);
    }

    @Override
    public void deleteReviewById(Long id, Long landmarkId) {
//        HistoricLandmark landmark = this.historicLandmarkRepository.findById(landmarkId).orElseThrow(InvalidLandmarkIdException::new);
//        landmark.getReviews().removeIf(r -> r.getId().equals(id));
//        this.historicLandmarkRepository.save(landmark);
        this.reviewRepository.deleteById(id);
    }

    @Override
    public Review findReviewById(Long id) {
        return this.reviewRepository.findById(id).orElseThrow(InvalidReviewIdException::new);
    }
}
