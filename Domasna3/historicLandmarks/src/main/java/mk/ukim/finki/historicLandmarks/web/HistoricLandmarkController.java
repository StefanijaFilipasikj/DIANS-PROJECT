package mk.ukim.finki.historicLandmarks.web;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.model.User;
import mk.ukim.finki.historicLandmarks.model.enumerations.UserRoles;
import mk.ukim.finki.historicLandmarks.repository.UserRepository;
import mk.ukim.finki.historicLandmarks.service.AuthService;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/database")
@Validated
@CrossOrigin(origins="*")
public class HistoricLandmarkController {

    private final HistoricLandmarkService historicLandmarkService;
    private final AuthService authService;
    public HistoricLandmarkController(HistoricLandmarkService historicLandmarkService, UserRepository userRepository, AuthService authService) {
        this.historicLandmarkService = historicLandmarkService;
        this.authService = authService;
    }

    @RequestMapping(path = "/feedData")
    public void setDataInDB(){
        //in case someone searches /database/feedData more than once we delete than load the database.
        //if you see this comment the next two lines so that there aren't any unnecessary deletes and loads.
        historicLandmarkService.deleteAllData();
        historicLandmarkService.saveData();
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<HistoricLandmark>> GetDatabase(){
        ResponseEntity<List<HistoricLandmark>> responseEntity = new ResponseEntity<>(historicLandmarkService.findAll(), HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> GetUsers(){

//        authService.register("username@admin", "DIANS", "DIANS", "Dians", "Proekt", "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg");

        ResponseEntity<List<User>> responseEntity = new ResponseEntity<>(authService.findAll(), HttpStatus.OK);
        return responseEntity;
    }

}
