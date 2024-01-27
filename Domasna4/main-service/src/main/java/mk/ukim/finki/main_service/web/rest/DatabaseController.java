//package mk.ukim.finki.main_service.web.rest;
//
//import mk.ukim.finki.main_service.model.HistoricLandmark;
//import mk.ukim.finki.main_service.model.User;
//import mk.ukim.finki.main_service.service.HistoricLandmarkService;
//import mk.ukim.finki.main_service.service.UserService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import java.util.List;
//
//@RestController
//@RequestMapping(value = "/database")
//@Validated
//@CrossOrigin(origins="*")
//public class DatabaseController {
//
//    private final HistoricLandmarkService historicLandmarkService;
//    private final UserService userService;
//
//    public DatabaseController(HistoricLandmarkService historicLandmarkService, UserService userService) {
//        this.historicLandmarkService = historicLandmarkService;
//        this.userService = userService;
//    }
//
//
//    @GetMapping(value = "/landmarks")
//    public ResponseEntity<List<HistoricLandmark>> GetAllLandmarksInDatabase(){
//        return new ResponseEntity<>(historicLandmarkService.findAllLandmarks(), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/users")
//    public ResponseEntity<List<User>> GetAllUsersInDatabase(){
//         return new ResponseEntity<>(this.userService.findAllUsers(), HttpStatus.OK);
//    }
//
//}
