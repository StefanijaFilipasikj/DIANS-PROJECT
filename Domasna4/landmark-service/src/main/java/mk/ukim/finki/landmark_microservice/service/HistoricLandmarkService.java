package mk.ukim.finki.landmark_microservice.service;

import mk.ukim.finki.landmark_microservice.model.HistoricLandmark;
import mk.ukim.finki.landmark_microservice.model.Review;

import java.util.List;

public interface HistoricLandmarkService {
    List<HistoricLandmark> findAllLandmarks();
    HistoricLandmark findLandmarkById(Long id);
    List<String> findAllRegions();
    List<String> findAllHistoricClassesRaw();
    List<String> findAllHistoricClassesCapitalizedAndSorted();
    void addNewLandmark(String name,String landmarkClass,Double lat,Double lon,String region,String address, String photoUrl);
    void editLandmarkById(Long landmarkId,String name,String landmarkClass,Double lat,Double lon,String region,String address, String photoUrl);
    void deleteLandmarkById(Long id);
    List<HistoricLandmark> findTop10Landmarks();
    HistoricLandmark findRandomLandmark();
    List<HistoricLandmark> filterBy(String text, String region, String historicClass);

    HistoricLandmark saveReviewToLandmark(Long revId, Long landmarkId);

    HistoricLandmark deleteReviewFromLandmark(Long revId, Long landmarkId);
}
