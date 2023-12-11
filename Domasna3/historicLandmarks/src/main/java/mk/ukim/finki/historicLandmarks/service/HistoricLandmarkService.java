package mk.ukim.finki.historicLandmarks.service;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;

import java.util.List;
import java.util.Optional;

public interface HistoricLandmarkService {

    void saveData();
    void deleteAllData();
    List<HistoricLandmark> findAll();
    Optional<HistoricLandmark> findById(Long id);
    List<String> findAllRegions();
    List<String> findAllHistoricClass();
    List<String> findAllHistoricClassRaw();
    List<String> capitalize(List<String> list);
    String removeCapitalize(String s);
    List<HistoricLandmark> searchByName(String text);
    Optional<HistoricLandmark> edit(String landmarkId,String name,String landmarkClass,String lat,String lon,String region,String address);
    Optional<HistoricLandmark> save(String name,String landmarkClass,String lat,String lon,String region,String address);
    void delete(Long id);
}
