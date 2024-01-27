package mk.ukim.finki.landmark_microservice.web;

import mk.ukim.finki.landmark_microservice.model.HistoricLandmark;
import mk.ukim.finki.landmark_microservice.service.HistoricLandmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MapController {
    private final HistoricLandmarkService historicLandmarkService;

    public MapController(HistoricLandmarkService historicLandmarkService) {
        this.historicLandmarkService = historicLandmarkService;
    }

    @GetMapping("/landmarks")
    public ResponseEntity<List<HistoricLandmark>> getFilteredLandmarks(
                                @RequestParam(required = false) String text,
                                @RequestParam(required = false) String region,
                                @RequestParam(required = false) String historicClass){
        List<HistoricLandmark> landmarks;
        try{
            landmarks = this.historicLandmarkService.filterBy(text, region, historicClass);
        }catch (Exception exception){
            landmarks = new ArrayList<>();
        }
        return ResponseEntity.ok().body(landmarks);
    }

    @GetMapping("/landmark/{id}")
    public ResponseEntity<HistoricLandmark> getLandmarkById(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(this.historicLandmarkService.findLandmarkById(id));
        }catch (Exception exception){
            return ResponseEntity.notFound().header("error", exception.getMessage()).build();
        }
    }

    @GetMapping("/top-10-landmarks")
    public ResponseEntity<List<HistoricLandmark>> getTop10Landmarks(){
        return ResponseEntity.ok().body(historicLandmarkService.findTop10Landmarks());
    }

    @GetMapping("/random-landmark")
    public ResponseEntity<List<HistoricLandmark>> getRandomLandmark(){
        return ResponseEntity.ok().body(List.of(this.historicLandmarkService.findRandomLandmark()));
    }

    @PostMapping("/add-new-landmark")
    public ResponseEntity<String> addNewLandmark(
                                @RequestParam String name,
                                @RequestParam String landmarkClass,
                                @RequestParam Double lat,
                                @RequestParam Double lon,
                                @RequestParam String region,
                                @RequestParam String address,
                                @RequestParam(required = false) String photoUrl){
        try{
            historicLandmarkService.addNewLandmark(name, landmarkClass,lat, lon, region, address, photoUrl);
        }catch (Exception exception){
            return ResponseEntity.ok().body(exception.getMessage());
        }
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/edit-landmark/{id}")
    public ResponseEntity<String> editLandmarkById(
                                @PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam String landmarkClass,
                                @RequestParam Double lat,
                                @RequestParam Double lon,
                                @RequestParam String region,
                                @RequestParam String address,
                                @RequestParam(required = false) String photoUrl){
        try{
            historicLandmarkService.editLandmarkById(id,name,landmarkClass,lat,lon,region,address, photoUrl);
        }catch (Exception exception){
            return ResponseEntity.ok().body(exception.getMessage());
        }
        return ResponseEntity.ok().body("");
    }

    @GetMapping("/delete-landmark/{id}")
    public void deleteLandmark(@PathVariable Long id){
        historicLandmarkService.deleteLandmarkById(id);
    }

    @GetMapping("/all-regions")
    public ResponseEntity<List<String>> getAllRegions(){
        return ResponseEntity.ok().body(historicLandmarkService.findAllRegions());
    }

    @GetMapping("/all-historic-classes")
    public ResponseEntity<List<String>> getAllHistoricClasses(@RequestParam(required = false) boolean sortedAndCapitalized){
        if(sortedAndCapitalized){
            return ResponseEntity.ok().body(historicLandmarkService.findAllHistoricClassesCapitalizedAndSorted());
        }else {
            return ResponseEntity.ok().body(historicLandmarkService.findAllHistoricClassesRaw());
        }
    }

    @PostMapping("/save-review-to-landmark/{revId}/{landmarkId}")
    public ResponseEntity<HistoricLandmark> saveReviewToLandmark(@PathVariable Long revId, @PathVariable Long landmarkId){
        return ResponseEntity.ok().body(historicLandmarkService.saveReviewToLandmark(revId, landmarkId));
    }

    @PostMapping("/delete-review-from-landmark/{revId}/{landmarkId}")
    public ResponseEntity<HistoricLandmark> deleteReviewFromLandmark(@PathVariable Long revId, @PathVariable Long landmarkId){
        return ResponseEntity.ok().body(historicLandmarkService.deleteReviewFromLandmark(revId, landmarkId));
    }
}
