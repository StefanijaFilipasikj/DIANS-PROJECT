package mk.ukim.finki.historicLandmarks.web;

import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.model.enumerations.Role;
import mk.ukim.finki.historicLandmarks.service.AuthService;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/map")
public class MapController {
    private final HistoricLandmarkService historicLandmarkService;
    private final AuthService authService;

    public MapController(HistoricLandmarkService historicLandmarkService, AuthService authService) {
        this.historicLandmarkService = historicLandmarkService;
        this.authService = authService;
    }

    @GetMapping
    public String getPage(@RequestParam(required = false) String text,
                          @RequestParam(required = false) String region,
                          @RequestParam(required = false) String historicClass,
                          @RequestParam(required = false) String random,
                          Model model){
        List<HistoricLandmark> landmarks = historicLandmarkService.findAll();

        if(text != null && !text.equals("")){
            landmarks = historicLandmarkService.searchByName(text);
        }
        if(region != null && !region.equals("")){
            landmarks = landmarks.stream().filter(h -> h.getRegion().equals(region)).toList();
        }
        if(historicClass != null && !historicClass.equals("")){
            landmarks = landmarks.stream().filter(h -> h.getHistoricClass()
                    .equals(historicLandmarkService.removeCapitalize(historicClass))).toList();
        }
        if(random != null && random.equals("true")){
            landmarks = new ArrayList<>();
            landmarks.add(this.historicLandmarkService.findRandomLandmark());
        }
        if(landmarks.isEmpty()){
            model.addAttribute("hasAny", false);
            landmarks = historicLandmarkService.findAll();
        }else{
            model.addAttribute("hasAny", true);
        }

        model.addAttribute("landmarks", landmarks);
        model.addAttribute("admin", Role.ROLE_ADMIN);
        model.addAttribute("regions", historicLandmarkService.findAllRegions().stream());
        model.addAttribute("historicClasses", historicLandmarkService.findAllHistoricClass());
        model.addAttribute("bodyContent", "map-page");
        return "master-template";
    }

    @GetMapping("/random")
    public String getRandom(    ){
        return "redirect:/map?random=true";
    }

    @GetMapping("/edit-list")
    public String getEditListPage(@RequestParam(required = false) String error,
                                  @RequestParam(required = false) String text,
                                  @RequestParam(required = false) String region,
                                  @RequestParam(required = false) String historicClass,
                                  Model model){
        List<HistoricLandmark> landmarks = historicLandmarkService.findAll();
        if (error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }
        if(text != null && !text.equals("")){
            landmarks = historicLandmarkService.searchByName(text);
        }
        if(region != null && !region.equals("")){
            landmarks = landmarks.stream().filter(h -> h.getRegion().equals(region)).toList();
        }
        if(historicClass != null && !historicClass.equals("")){
            landmarks = landmarks.stream().filter(h -> h.getHistoricClass()
                    .equals(historicLandmarkService.removeCapitalize(historicClass))).toList();
        }
        if(landmarks.isEmpty()){
            model.addAttribute("hasAny", false);
            landmarks = historicLandmarkService.findAll();
        }else{
            model.addAttribute("hasAny", true);
        }
        model.addAttribute("landmarks", landmarks);
        model.addAttribute("regions", historicLandmarkService.findAllRegions().stream());
        model.addAttribute("historicClasses", historicLandmarkService.findAllHistoricClass());
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
        model.addAttribute("historicClasses", historicLandmarkService.findAllHistoricClassRaw());
        model.addAttribute("bodyContent", "add-landmark");
        return "master-template";
    }

    @PostMapping("/add")
    public String addLandmark(@RequestParam String name,
                              @RequestParam String landmarkClass,
                              @RequestParam String lat,
                              @RequestParam String lon,
                              @RequestParam String region,
                              @RequestParam String address,
                              @RequestParam String landmarkId,
                              @RequestParam String photoUrl){
        if (landmarkId!=null && !landmarkId.isEmpty()){
            historicLandmarkService.edit(landmarkId,name,landmarkClass,lat,lon,region,address, photoUrl);
        }
        else {
            historicLandmarkService.save(lat,lon,landmarkClass,name,address,region, photoUrl);
        }
        return "redirect:/map/edit-list";
    }


    @GetMapping("/add-landmark/{id}")
    public String addLandmark(@PathVariable Long id, Model model){
        if(historicLandmarkService.findById(id).isPresent()){
            HistoricLandmark landmark = historicLandmarkService.findById(id).get();
            model.addAttribute("landmark",landmark);
            model.addAttribute("historicClasses", historicLandmarkService.findAllHistoricClassRaw());
            model.addAttribute("bodyContent","add-landmark");
            return "master-template";
        }
        return "redirect:/edit-list?error=Landmark%20Not%20Found";
    }

    @GetMapping("/delete-landmark/{id}")
    public String deleteLandmark(@PathVariable Long id,Model model){
        historicLandmarkService.delete(id);
        return "redirect:/map/edit-list";
    }
}
