package mk.ukim.finki.historicLandmarks.repository;

import jakarta.transaction.Transactional;
import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoricLandmarkRepository extends JpaRepository<HistoricLandmark,Long> {

    @Transactional
    default Optional<HistoricLandmark> editLandmarkById(String landmarkId, String name, String landmarkClass, String lat, String lon, String address, String region, String photoUrl) {
        Optional<HistoricLandmark> optionalLandmark = findById(Long.parseLong(landmarkId));

        optionalLandmark.ifPresent(landmark -> {
            landmark.setName(name);
            landmark.setHistoricClass(landmarkClass);
            landmark.setLon(Double.parseDouble(lon));
            landmark.setLat(Double.parseDouble(lat));
            landmark.setAddress(address);
            landmark.setRegion(region);
            landmark.setPhotoUrl(photoUrl);
            save(landmark);
        });

        return optionalLandmark;
    }
}
