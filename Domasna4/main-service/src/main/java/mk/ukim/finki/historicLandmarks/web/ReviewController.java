//package mk.ukim.finki.historicLandmarks.web;
//
//import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
//import mk.ukim.finki.historicLandmarks.model.Review;
//import mk.ukim.finki.historicLandmarks.model.User;
//import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
//import mk.ukim.finki.historicLandmarks.service.ReviewService;
//import mk.ukim.finki.historicLandmarks.service.UserService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/review")
//public class ReviewController {
//
//    private final ReviewService reviewService;
//    private final HistoricLandmarkService historicLandmarkService;
//    private final UserService userService;
//
//    public ReviewController(ReviewService reviewService, HistoricLandmarkService historicLandmarkService, UserService userService) {
//        this.reviewService = reviewService;
//        this.historicLandmarkService = historicLandmarkService;
//        this.userService = userService;
//    }
//
//    private Map<String, String> createReviewMap(Review review, HistoricLandmark landmark, String comment, Double rating){
//        Map<String, String> map = new HashMap<>();
//        map.put("comment", comment);
//        map.put("rating", rating.toString());
//        map.put("id", review.getId().toString());
//        map.put("landmarkId", landmark.getId().toString());
//        map.put("avgRating", landmark.getRating().toString());
//        return map;
//    }
//
//    @PostMapping("/add-review/{id}")
//    @ResponseBody
//    public ResponseEntity<Map<String, String>> addReviewToLandmark(@PathVariable Long id,
//                                                                   @RequestParam String comment,
//                                                                   @RequestParam Double rating,
//                                                                   Authentication authentication){
//        HistoricLandmark landmark = historicLandmarkService.findLandmarkById(id);
//        User user = this.userService.findUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
//        if (landmark != null && user != null) {
//            Review rev = reviewService.addNewReview(landmark, user, comment, rating);
//            Map<String, String> map = createReviewMap(rev, landmark, comment, rating);
//            map.put("photoUrl", user.getPhotoUrl());
//            map.put("username", user.getUsername());
//            map.put("numReviews", landmark.getNumberOfReviews().toString());
//            return ResponseEntity.ok().body(map);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//    @PostMapping("/edit/{id}/{landmarkId}")
//    @ResponseBody
//    public ResponseEntity<Map<String, String>> editReview(@PathVariable Long id,
//                                                          @PathVariable Long landmarkId,
//                                                          @RequestParam String editComment,
//                                                          @RequestParam Double editRating){
//        if(editRating != null && !editComment.isEmpty()){
//            Review review = reviewService.findReviewById(id);
//            HistoricLandmark landmark = historicLandmarkService.findLandmarkById(landmarkId);
//            this.reviewService.editReviewById(id, editRating, editComment);
//            return ResponseEntity.ok().body(createReviewMap(review, landmark, editComment, editRating));
//        }else{
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PostMapping("/delete/{id}/{landmarkId}")
//    @ResponseBody
//    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable Long id,
//                                                            @PathVariable Long landmarkId){
//        HistoricLandmark landmark = historicLandmarkService.findLandmarkById(landmarkId);
//        if(landmark != null){
//            this.reviewService.deleteReviewById(id, landmarkId);
//            Map<String, String> map = new HashMap<>();
//            map.put("avgRating", landmark.getRating().toString());
//            map.put("numReviews", landmark.getNumberOfReviews().toString());
//            return ResponseEntity.ok().body(map);
//        }else{
//            return ResponseEntity.notFound().build();
//        }
//    }
//}
