package mk.ukim.finki.main_service.service.impl;

import mk.ukim.finki.main_service.model.User;
import mk.ukim.finki.main_service.model.enumerations.Role;
import mk.ukim.finki.main_service.model.exception.InvalidInputsException;
import mk.ukim.finki.main_service.model.exception.InvalidUserCredentialsException;
import mk.ukim.finki.main_service.model.exception.PasswordsDoNotMatchException;
import mk.ukim.finki.main_service.model.exception.UsernameAlreadyExistsException;
import mk.ukim.finki.main_service.repository.UserRepository;
import mk.ukim.finki.main_service.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return this.userRepository.findById(username).orElseThrow(InvalidUserCredentialsException::new);
    }

    @Override
    public void register(String username, String password, String repeatPassword, String name, String surname, String photoUrl, Role role) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new InvalidUserCredentialsException();
        }

        if(this.userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        if (!password.equals(repeatPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        try{
            URI.create(photoUrl).toURL();
        }catch (Exception exception){
            throw new InvalidInputsException("Invalid Photo URL");
        }

        userRepository.save(new User(username, passwordEncoder.encode(password), name, surname, photoUrl, role));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
    }
}

