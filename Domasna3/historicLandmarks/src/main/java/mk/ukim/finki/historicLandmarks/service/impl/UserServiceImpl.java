package mk.ukim.finki.historicLandmarks.service.impl;

import mk.ukim.finki.historicLandmarks.model.User;
import mk.ukim.finki.historicLandmarks.model.enumerations.Role;
import mk.ukim.finki.historicLandmarks.model.exception.InvalidUserCredentialsException;
import mk.ukim.finki.historicLandmarks.model.exception.PasswordsDoNotMatchException;
import mk.ukim.finki.historicLandmarks.model.exception.UsernameAlreadyExistsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import mk.ukim.finki.historicLandmarks.repository.UserRepository;
import mk.ukim.finki.historicLandmarks.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String username, String password, String repeatPassword, String name, String surname, String photoUrl) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new InvalidUserCredentialsException();
        }

        if (!password.equals(repeatPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if(this.userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        User user = new User(username, passwordEncoder.encode(password), name, surname, photoUrl);
        if(username.equals("admin")) user.setRole(Role.ROLE_ADMIN);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
    }

    @Override
    public void deleteAllData() {
        this.userRepository.deleteAll();
    }

    @Override
    public void addInitialData() {
        User admin = this.register("admin", "admin", "admin", "Admin", "Admin", "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg");
        this.register("user", "user", "user", "Dians", "Dians", "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg");
    }

    @Override
    public boolean empty() {
        return this.userRepository.count() == 0;
    }

}

