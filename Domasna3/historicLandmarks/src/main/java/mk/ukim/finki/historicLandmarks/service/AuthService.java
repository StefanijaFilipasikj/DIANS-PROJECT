package mk.ukim.finki.historicLandmarks.service;

import mk.ukim.finki.historicLandmarks.model.User;

import java.util.List;
import java.util.Optional;

public interface AuthService {
    User login(String username, String password);
    List<User> findAll();
    User findByUsername(String username);
}


