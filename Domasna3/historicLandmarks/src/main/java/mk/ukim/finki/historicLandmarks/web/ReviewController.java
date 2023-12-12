package mk.ukim.finki.historicLandmarks.web;

import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import mk.ukim.finki.historicLandmarks.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final HistoricLandmarkService historicLandmarkService;

    public ReviewController(ReviewService reviewService, HistoricLandmarkService historicLandmarkService) {
        this.reviewService = reviewService;
        this.historicLandmarkService = historicLandmarkService;
    }

    @PostMapping("/edit/{id}")
    public String editReview(@PathVariable Long id, @RequestParam String editComment, @RequestParam Double editRating){
        this.reviewService.edit(id, editRating, editComment);
        return "redirect:/map";
    }
    @GetMapping("/delete/{id}/{landmarkId}")
    public String deleteReview(@PathVariable Long id, @PathVariable Long landmarkId){
        this.reviewService.deleteById(id, landmarkId);
        return "redirect:/map";
    }

}
