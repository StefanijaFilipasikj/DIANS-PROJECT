package mk.ukim.finki.main_service.config;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.main_service.model.enumerations.Role;
import mk.ukim.finki.main_service.repository.UserRepository;
import mk.ukim.finki.main_service.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final UserService userService;
    private final UserRepository userRepository;

    public DataInitializer(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initData() {
        if(this.isEmpty()){
            this.deleteAllData();
            this.addInitialData();
        }
    }

    public void addInitialData(){
        this.userService.register("admin", "admin", "admin", "Admin", "Admin", "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg", Role.ROLE_ADMIN);
        this.userService.register("user", "user", "user", "User", "User", "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg", Role.ROLE_USER);
    }

    public void deleteAllData() {
        this.userRepository.deleteAll();
    }

    public boolean isEmpty() {
        return this.userRepository.count() == 0;
    }
}