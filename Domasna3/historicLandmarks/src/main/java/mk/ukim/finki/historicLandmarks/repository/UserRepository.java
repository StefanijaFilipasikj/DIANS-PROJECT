package mk.ukim.finki.historicLandmarks.repository;

import mk.ukim.finki.historicLandmarks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUsername(String username);
}
