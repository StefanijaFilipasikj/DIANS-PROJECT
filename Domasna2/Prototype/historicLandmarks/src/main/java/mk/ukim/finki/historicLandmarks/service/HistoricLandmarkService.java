package mk.ukim.finki.historicLandmarks.service;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;

import java.util.List;

public interface HistoricLandmarkService {

    void saveData();
    void deleteAllData();
    List<HistoricLandmark> findAll();
    List<String> findAllRegions();
    List<String> findAllHistoricClass();
    List<String> capitalize(List<String> list);
    String removeCapitalize(String s);
    List<HistoricLandmark> searchByName(String text);
}
