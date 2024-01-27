package mk.ukim.finki.main_service.web.rest;

import jakarta.transaction.Transactional;
import mk.ukim.finki.main_service.helper.RequestHelper;
import mk.ukim.finki.main_service.model.HistoricLandmark;
import mk.ukim.finki.main_service.model.Review;
import mk.ukim.finki.main_service.model.User;
import mk.ukim.finki.main_service.service.ReviewService;
import mk.ukim.finki.main_service.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id){
        return ResponseEntity.ok().body(this.reviewService.findReviewById(id));
    }

    @PostMapping("/add-review/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addReviewToLandmark(@PathVariable Long id,
                                                                   @RequestParam String comment,
                                                                   @RequestParam Double rating,
                                                                   Authentication authentication){
        HistoricLandmark landmark = Objects.requireNonNull(RequestHelper.sendGetRequestForLandmark(RequestHelper.createRequestUrl("/landmark/"+id, new HashMap<>())).getBody());
        User user = this.userService.findUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
        if (user != null) {
            Review rev = this.reviewService.addNewReview(landmark, user, comment, rating);
            String url = String.format("/save-review-to-landmark/%d/%d", rev.getId(), id);
            landmark = RequestHelper.sendPostRequestForLandmark(RequestHelper.createRequestUrl(url, new HashMap<>()), new HttpEntity<>(new LinkedMultiValueMap<>()));

            Map<String, String> map = createReviewMap(rev, landmark, comment, rating);
            map.put("photoUrl", user.getPhotoUrl());
            map.put("username", user.getUsername());
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
        if(editRating != null && !editComment.isEmpty()){
            Review review = reviewService.findReviewById(id);
            this.reviewService.editReviewById(id, editRating, editComment);
            String url = String.format("/save-review-to-landmark/%d/%d", id, landmarkId);
            HistoricLandmark landmark = RequestHelper.sendPostRequestForLandmark(RequestHelper.createRequestUrl(url, new HashMap<>()), new HttpEntity<>(new LinkedMultiValueMap<>()));
            return ResponseEntity.ok().body(createReviewMap(review, landmark, editComment, editRating));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    private Map<String, String> createReviewMap(Review review, HistoricLandmark landmark, String comment, Double rating){
        Map<String, String> map = new HashMap<>();
        map.put("comment", comment);
        map.put("rating", rating.toString());
        map.put("id", review.getId().toString());
        map.put("landmarkId", landmark.getId().toString());
        map.put("avgRating", landmark.getRating().toString());
        map.put("numReviews", landmark.getNumberOfReviews().toString());
        return map;
    }

    @PostMapping("/delete/{id}/{landmarkId}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable Long id, @PathVariable Long landmarkId){
        this.reviewService.deleteReviewById(id, landmarkId);
        String url = String.format("/delete-review-from-landmark/%d/%d", id, landmarkId);
        HistoricLandmark landmark = RequestHelper.sendPostRequestForLandmark(RequestHelper.createRequestUrl(url, new HashMap<>()), new HttpEntity<>(new LinkedMultiValueMap<>()));
        Map<String, String> map = new HashMap<>();
        map.put("avgRating", landmark.getRating().toString());
        map.put("numReviews", landmark.getNumberOfReviews().toString());
        return ResponseEntity.ok().body(map);
    }
}
