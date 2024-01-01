package mk.ukim.finki.historicLandmarks.service;

import mk.ukim.finki.historicLandmarks.model.User;
import mk.ukim.finki.historicLandmarks.model.enumerations.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> findAllUsers();
    User findUserByUsername(String username);
    void register(String username, String password, String repeatPassword, String name, String surname, String photoUrl, Role role);
}
