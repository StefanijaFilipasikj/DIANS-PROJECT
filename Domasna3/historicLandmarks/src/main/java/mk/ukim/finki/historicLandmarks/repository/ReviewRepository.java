package mk.ukim.finki.historicLandmarks.repository;

import mk.ukim.finki.historicLandmarks.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
