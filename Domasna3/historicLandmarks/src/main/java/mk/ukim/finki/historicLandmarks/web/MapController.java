package mk.ukim.finki.historicLandmarks.web;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/map")
public class MapController {
    private final HistoricLandmarkService historicLandmarkService;

    public MapController(HistoricLandmarkService historicLandmarkService) {
        this.historicLandmarkService = historicLandmarkService;
    }

    @GetMapping
    public String getPage(@RequestParam(required = false) String text,
                          @RequestParam(required = false) String region,
                          @RequestParam(required = false) String historicClass,
                          @RequestParam(required = false) String random,
                          Model model){

        List<HistoricLandmark> landmarks = this.historicLandmarkService.findAllLandmarks();
        if(text != null || region != null || historicClass != null){
            landmarks = this.historicLandmarkService.filterBy(text, region, historicClass);
        }
        if(random != null && random.equals("true")){
            landmarks = List.of(this.historicLandmarkService.findRandomLandmark());
        }

        //if after filtering, the list is empty, set hasAny to false
        if(landmarks.isEmpty()){
            model.addAttribute("hasAny", false);
            landmarks = this.historicLandmarkService.findAllLandmarks();
        }else{
            model.addAttribute("hasAny", true);
        }

        model.addAttribute("landmarks", landmarks);
        model.addAttribute("regions", this.historicLandmarkService.findAllRegions());
        model.addAttribute("historicClasses", this.historicLandmarkService.findAllHistoricClassesCapitalizedAndSorted());
        model.addAttribute("bodyContent", "map-page");
        return "master-template";
    }

    @GetMapping("/random")
    public String getRandom(){
        return "redirect:/map?random=true";
    }

    @GetMapping("/edit-list")
    public String getEditListPage(@RequestParam(required = false) String error, Model model){
        if (error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }
        model.addAttribute("hasAny", true);
        model.addAttribute("landmarks", this.historicLandmarkService.findAllLandmarks());
        model.addAttribute("regions", this.historicLandmarkService.findAllRegions().stream());
        model.addAttribute("historicClasses", this.historicLandmarkService.findAllHistoricClassesCapitalizedAndSorted());
        model.addAttribute("bodyContent", "edit-list");
        return "master-template";
    }

    @GetMapping("/add-landmark")
    public String addLandmarkPage(@RequestParam(required = false) String error,Model model){
        if (error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }
        model.addAttribute("add", true);
        model.addAttribute("historicClasses", this.historicLandmarkService.findAllHistoricClassesRaw());
        model.addAttribute("bodyContent", "add-landmark");
        return "master-template";
    }

    @PostMapping("/add")
    public String addLandmark(@RequestParam(required = false) Long landmarkId,
                              @RequestParam String name,
                              @RequestParam String landmarkClass,
                              @RequestParam Double lat,
                              @RequestParam Double lon,
                              @RequestParam String region,
                              @RequestParam String address,
                              @RequestParam String photoUrl){
        if (landmarkId!=null){
            try{
                historicLandmarkService.editLandmarkById(landmarkId,name,landmarkClass,lat,lon,region,address, photoUrl);
            }catch (Exception exception){
                return "redirect:/map/add-landmark/" + String.format("%d?error=%s", landmarkId, exception.getMessage());
            }
        }
        else {
            try{
                historicLandmarkService.addNewLandmark(name, landmarkClass,lat, lon, region, address, photoUrl);
            }catch (Exception exception){
                return "redirect:/map/add-landmark?error=" + exception.getMessage();
            }
        }
        return "redirect:/map/edit-list";
    }


    @GetMapping("/add-landmark/{id}")
    public String addLandmark(@PathVariable Long id,
                              @RequestParam(required = false) String error,
                              Model model){
        if (error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }
        try{
            model.addAttribute("landmark",this.historicLandmarkService.findLandmarkById(id));
            model.addAttribute("historicClasses", this.historicLandmarkService.findAllHistoricClassesRaw());
            model.addAttribute("bodyContent","add-landmark");
            return "master-template";
        }catch (Exception exception){
            return "redirect:/edit-list?error=" + exception.getMessage();
        }
    }

    @GetMapping("/delete-landmark/{id}")
    public String deleteLandmark(@PathVariable Long id){
        historicLandmarkService.deleteLandmarkById(id);
        return "redirect:/map/edit-list";
    }
}
