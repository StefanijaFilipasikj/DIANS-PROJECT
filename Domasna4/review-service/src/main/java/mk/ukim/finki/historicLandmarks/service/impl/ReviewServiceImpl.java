package mk.ukim.finki.historicLandmarks.service.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.model.Review;
import mk.ukim.finki.historicLandmarks.model.User;
import mk.ukim.finki.historicLandmarks.model.exception.InvalidLandmarkIdException;
import mk.ukim.finki.historicLandmarks.model.exception.InvalidReviewIdException;
import mk.ukim.finki.historicLandmarks.repository.HistoricLandmarkRepository;
import mk.ukim.finki.historicLandmarks.repository.ReviewRepository;
import mk.ukim.finki.historicLandmarks.service.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final HistoricLandmarkRepository historicLandmarkRepository;
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(HistoricLandmarkRepository historicLandmarkRepository,
                             ReviewRepository reviewRepository) {
        this.historicLandmarkRepository = historicLandmarkRepository;
        this.reviewRepository = reviewRepository;
    }
    @Override
    @Transactional
    public Review addNewReview(HistoricLandmark landmark, User user, String comment, Double rating) {
        Review review = new Review(user, comment, rating);
        reviewRepository.save(review);
        landmark.getReviews().add(review);
        historicLandmarkRepository.save(landmark);
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
        HistoricLandmark landmark = this.historicLandmarkRepository.findById(landmarkId).orElseThrow(InvalidLandmarkIdException::new);
        landmark.getReviews().removeIf(r -> r.getId().equals(id));
        this.historicLandmarkRepository.save(landmark);
        this.reviewRepository.deleteById(id);
    }

    @Override
    public Review findReviewById(Long id) {
        return this.reviewRepository.findById(id).orElseThrow(InvalidReviewIdException::new);
    }
}
