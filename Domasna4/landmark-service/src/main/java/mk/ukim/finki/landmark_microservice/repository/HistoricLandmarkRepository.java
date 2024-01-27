package mk.ukim.finki.landmark_microservice.repository;

import jakarta.transaction.Transactional;
import mk.ukim.finki.landmark_microservice.model.HistoricLandmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface HistoricLandmarkRepository extends JpaRepository<HistoricLandmark,Long> {
    List<HistoricLandmark> findByNameContainingIgnoreCase(String name);
    List<HistoricLandmark> findByRegionIgnoreCase(String region);
    List<HistoricLandmark> findByHistoricClassIgnoreCase(String historicClass);
    List<HistoricLandmark> findByNameContainingAndRegionAllIgnoreCase(String name, String region);
    List<HistoricLandmark> findByNameContainingAndHistoricClassAllIgnoreCase(String name, String historicClass);
    List<HistoricLandmark> findByRegionAndHistoricClassAllIgnoreCase(String region, String historicClass);
    List<HistoricLandmark> findByNameContainingAndRegionAndHistoricClassAllIgnoreCase(String name, String region, String historicClass);
}
