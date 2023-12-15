package mk.ukim.finki.historicLandmarks.service;

import mk.ukim.finki.historicLandmarks.model.User;

import java.util.List;

public interface AuthService {
    User login(String username, String password);
    User register(String username, String password, String repeatPassword, String name, String surname, String photoUrl);
    List<User> findAll();
}


