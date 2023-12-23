package mk.ukim.finki.historicLandmarks.web;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.model.User;
import mk.ukim.finki.historicLandmarks.model.enumerations.Role;
import mk.ukim.finki.historicLandmarks.repository.UserRepository;
import mk.ukim.finki.historicLandmarks.service.AuthService;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import mk.ukim.finki.historicLandmarks.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/database")
@Validated
@CrossOrigin(origins="*")
public class HistoricLandmarkController {

    private final HistoricLandmarkService historicLandmarkService;
    private final UserService userService;
    private final AuthService authService;
    private final UserRepository userRepository;
    public HistoricLandmarkController(HistoricLandmarkService historicLandmarkService, UserService userService, AuthService authService, UserRepository userRepository) {
        this.historicLandmarkService = historicLandmarkService;
        this.userService = userService;
        this.authService = authService;
        this.userRepository = userRepository;
    }

//    @RequestMapping(path = "/feedData")
//    public void setDataInDB(){
//        //in case someone searches /database/feedData more than once we delete than load the database.
//        //if you see this comment the next two lines so that there aren't any unnecessary deletes and loads.
//        this.historicLandmarkService.deleteAllData();
//        this.historicLandmarkService.saveData();
//
//        this.userService.deleteAllData();
//        this.userService.addInitialData();
//    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<HistoricLandmark>> GetDatabase(){
        ResponseEntity<List<HistoricLandmark>> responseEntity = new ResponseEntity<>(historicLandmarkService.findAll(), HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> GetUsers(){
         ResponseEntity<List<User>> responseEntity = new ResponseEntity<>(this.authService.findAll(), HttpStatus.OK);
        return responseEntity;
    }

}
