package mk.ukim.finki.historicLandmarks.repository;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricLandmarkRepository extends JpaRepository<HistoricLandmark,Long> {

}
