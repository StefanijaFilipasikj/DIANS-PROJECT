package mk.ukim.finki.historicLandmarks.web;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.model.Review;
import mk.ukim.finki.historicLandmarks.model.User;
import mk.ukim.finki.historicLandmarks.model.exception.InvalidArgumentsException;
import mk.ukim.finki.historicLandmarks.service.AuthService;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import mk.ukim.finki.historicLandmarks.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final HistoricLandmarkService historicLandmarkService;
    private final AuthService authService;

    public ReviewController(ReviewService reviewService, HistoricLandmarkService historicLandmarkService, AuthService authService) {
        this.reviewService = reviewService;
        this.historicLandmarkService = historicLandmarkService;
        this.authService = authService;
    }

    @PostMapping("/add-review/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addReviewToLandmark(@PathVariable Long id,
                                                                   @RequestParam String comment,
                                                                   @RequestParam Double rating,
                                                                   Authentication authentication){
        HistoricLandmark landmark = historicLandmarkService.findById(id).orElseThrow(InvalidArgumentsException::new);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = this.authService.findByUsername(username);


        if (landmark != null && user != null) {
            Review rev = reviewService.addReview(landmark, user, comment, rating);

            Map<String, String> map = new HashMap<>();
            map.put("photoUrl", user.getPhotoUrl());
            map.put("username", user.getUsername());
            map.put("comment", comment);
            map.put("rating", rating.toString());
            map.put("id", rev.getId().toString());
            map.put("landmarkId", landmark.getId().toString());
            map.put("avgRating", landmark.getRating().toString());
            map.put("numReviews", landmark.getNumberOfReviews().toString());

            return ResponseEntity.ok().body(map);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/edit/{id}/{landmarkId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> editReview(@PathVariable Long id,
                                                          @PathVariable Long landmarkId,
                                                          @RequestParam String editComment,
                                                          @RequestParam Double editRating){
        Review review = reviewService.findById(id);
        HistoricLandmark landmark = historicLandmarkService.findById(landmarkId).orElseThrow(InvalidArgumentsException::new);
        User user = review.getUser();

        if(editComment != null){
            this.reviewService.edit(id, editRating, editComment);

            Map<String, String> map = new HashMap<>();
            map.put("photoUrl", user.getPhotoUrl());
            map.put("username", user.getUsername());
            map.put("comment", editComment);
            map.put("rating", editRating.toString());
            map.put("id", review.getId().toString());
            map.put("landmarkId", landmarkId.toString());
            map.put("avgRating", landmark.getRating().toString());
            map.put("numReviews", landmark.getNumberOfReviews().toString());

            return ResponseEntity.ok().body(map);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete/{id}/{landmarkId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable Long id,
                                                            @PathVariable Long landmarkId){
        HistoricLandmark landmark = historicLandmarkService.findById(landmarkId ).orElseThrow(InvalidArgumentsException::new);
        if(landmark != null){
            this.reviewService.deleteById(id, landmarkId);
            Map<String, String> map = new HashMap<>();
            map.put("avgRating", landmark.getRating().toString());
            map.put("numReviews", landmark.getNumberOfReviews().toString());
            return ResponseEntity.ok().body(map);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
