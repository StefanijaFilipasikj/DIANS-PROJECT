package mk.ukim.finki.historicLandmarks.service;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.model.Review;
import mk.ukim.finki.historicLandmarks.model.User;

public interface ReviewService {
    void addReview(HistoricLandmark landmark, User user, String comment, Double rating);
    void edit(Long id, Double rating, String comment);
    void deleteById(Long id, Long landmarkId);
}
