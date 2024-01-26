package mk.ukim.finki.historicLandmarks.repository;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricLandmarkRepository extends JpaRepository<HistoricLandmark,Long> {
    List<HistoricLandmark> findByNameContainingIgnoreCase(String name);
    List<HistoricLandmark> findByRegionIgnoreCase(String region);
    List<HistoricLandmark> findByHistoricClassIgnoreCase(String historicClass);
    List<HistoricLandmark> findByNameContainingIgnoreCaseAndRegionIgnoreCase(String name, String region);
    List<HistoricLandmark> findByNameContainingIgnoreCaseAndHistoricClassIgnoreCase(String name, String historicClass);
    List<HistoricLandmark> findByRegionIgnoreCaseAndHistoricClassIgnoreCase(String region, String historicClass);
    List<HistoricLandmark> findByNameContainingIgnoreCaseAndRegionIgnoreCaseAndHistoricClassIgnoreCase(String name, String region, String historicClass);
}
