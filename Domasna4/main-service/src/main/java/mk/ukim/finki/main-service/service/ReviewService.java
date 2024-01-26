package mk.ukim.finki.historicLandmarks.service;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.model.Review;
import mk.ukim.finki.historicLandmarks.model.User;

public interface ReviewService {
    Review addNewReview(HistoricLandmark landmark, User user, String comment, Double rating);
    void editReviewById(Long id, Double rating, String comment);
    void deleteReviewById(Long id, Long landmarkId);
    Review findReviewById(Long id);
}
