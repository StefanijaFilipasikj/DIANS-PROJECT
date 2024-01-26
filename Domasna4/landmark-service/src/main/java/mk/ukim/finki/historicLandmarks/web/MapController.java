package mk.ukim.finki.historicLandmarks.web;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {
    private final HistoricLandmarkService historicLandmarkService;

    public MapController(HistoricLandmarkService historicLandmarkService) {
        this.historicLandmarkService = historicLandmarkService;
    }

    @GetMapping
    public ResponseEntity<List<HistoricLandmark>> getFilteredLandmarks(@RequestParam(required = false) String text,
                          @RequestParam(required = false) String region,
                          @RequestParam(required = false) String historicClass){

        List<HistoricLandmark> landmarks = this.historicLandmarkService.filterBy(text, region, historicClass);

        if(landmarks.isEmpty()){
            landmarks = this.historicLandmarkService.findAllLandmarks();
        }
        return ResponseEntity.ok().body(landmarks);
    }

    @GetMapping("/random")
    public ResponseEntity<List<HistoricLandmark>> getRandom(@RequestParam String random){
        return ResponseEntity.ok().body(List.of(this.historicLandmarkService.findRandomLandmark()));
    }

    @GetMapping("/all-landmarks")
    public ResponseEntity<List<HistoricLandmark>> getAllLandmarks(){
        return ResponseEntity.ok().body(historicLandmarkService.findAllLandmarks());
    }

    @GetMapping("/top-10-landmarks")
    public ResponseEntity<List<HistoricLandmark>> getTop10Landmarks(){
        return ResponseEntity.ok().body(historicLandmarkService.findTop10Landmarks());
    }

    @GetMapping("/landmark/{id}")
    public ResponseEntity<HistoricLandmark> getLandmark(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(this.historicLandmarkService.findLandmarkById(id));
        }catch (Exception exception){
            return ResponseEntity.notFound().header("error", exception.getMessage()).build();
        }
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

    @PostMapping("/add-new-landmark")
    public ResponseEntity<String> addNewLandmark(@RequestParam String name,
                               @RequestParam String landmarkClass,
                               @RequestParam Double lat,
                               @RequestParam Double lon,
                               @RequestParam String region,
                               @RequestParam String address,
                               @RequestParam String photoUrl){
        try{
            historicLandmarkService.addNewLandmark(name, landmarkClass,lat, lon, region, address, photoUrl);
        }catch (Exception exception){
            return ResponseEntity.notFound().header("error", exception.getMessage()).build();
        }
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/edit-landmark/{id}")
    public ResponseEntity<String> addNewLandmark(@PathVariable Long id,
                                 @RequestParam String name,
                                 @RequestParam String landmarkClass,
                                 @RequestParam Double lat,
                                 @RequestParam Double lon,
                                 @RequestParam String region,
                                 @RequestParam String address,
                                 @RequestParam String photoUrl){
        try{
            historicLandmarkService.editLandmarkById(id,name,landmarkClass,lat,lon,region,address, photoUrl);
        }catch (Exception exception){
            return ResponseEntity.notFound().header("error", exception.getMessage()).build();
        }
        return ResponseEntity.ok().body("");
    }

    @PostMapping("/delete-landmark/{id}")
    public void deleteLandmark(@PathVariable Long id){
        historicLandmarkService.deleteLandmarkById(id);
    }
}
