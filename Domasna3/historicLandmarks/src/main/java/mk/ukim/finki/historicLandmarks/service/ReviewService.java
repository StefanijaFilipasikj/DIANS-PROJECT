package mk.ukim.finki.historicLandmarks.service;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.model.Review;
import mk.ukim.finki.historicLandmarks.model.User;

public interface ReviewService {
    Review addReview(HistoricLandmark landmark, User user, String comment, Double rating);
    void edit(Long id, Double rating, String comment);
    void deleteById(Long id, Long landmarkId);
    Review findById(Long id);
}
