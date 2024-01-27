package mk.ukim.finki.main_service.service;

import mk.ukim.finki.main_service.model.HistoricLandmark;
import mk.ukim.finki.main_service.model.Review;
import mk.ukim.finki.main_service.model.User;

public interface ReviewService {
    Review addNewReview(HistoricLandmark landmark, User user, String comment, Double rating);
    void editReviewById(Long id, Double rating, String comment);
    void deleteReviewById(Long id, Long landmarkId);
    Review findReviewById(Long id);
}
