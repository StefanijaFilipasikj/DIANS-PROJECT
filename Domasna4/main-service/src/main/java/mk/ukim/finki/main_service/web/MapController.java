package mk.ukim.finki.main_service.web;

import mk.ukim.finki.main_service.helper.RequestHelper;
import mk.ukim.finki.main_service.model.HistoricLandmark;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequestMapping("/map")
public class MapController {
    @GetMapping
    public String getPage(@RequestParam(required = false) String text,
                          @RequestParam(required = false) String region,
                          @RequestParam(required = false) String historicClass,
                          @RequestParam(required = false) String random,
                          @RequestParam(required = false) String details,
                          Model model){

        Map<String, String> paramsLandmarks = new HashMap<>();
        paramsLandmarks.put("text", text);
        paramsLandmarks.put("region", region);
        paramsLandmarks.put("historicClass", historicClass);
        List<HistoricLandmark> landmarks = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForLandmarks(RequestHelper.createRequestUrl("/landmarks", paramsLandmarks)).getBody()));

        if(random != null && random.equals("true")){
            landmarks = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForLandmarks(RequestHelper.createRequestUrl("/random-landmark", new HashMap<>())).getBody()));
        }

        if(details != null){
            landmarks = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForLandmark(RequestHelper.createRequestUrl("/landmark/" + details, new HashMap<>())).getBody()));
        }

        //if after filtering, the list is empty, set hasAny to false and get a list of all the landmarks
        //this is to display 'Nothing matched your search', but have location marks for all landmarks on the map
        if(landmarks.isEmpty()){
            model.addAttribute("hasAny", false);
            landmarks = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForLandmarks(RequestHelper.createRequestUrl("/landmarks", new HashMap<>())).getBody()));
        }else{
            model.addAttribute("hasAny", true);
        }

        List<String> regions = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForStrings(RequestHelper.createRequestUrl("/all-regions", new HashMap<>())).getBody()));
        HashMap<String, String> paramsSorted = new HashMap<>();
        paramsSorted.put("sortedAndCapitalized", "true");
        List<String> classes = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForStrings(RequestHelper.createRequestUrl("/all-historic-classes", paramsSorted)).getBody()));

        model.addAttribute("landmarks", landmarks);
        model.addAttribute("regions", regions);
        model.addAttribute("historicClasses", classes);
        model.addAttribute("bodyContent", "map-page");
        return "master-template";
    }

    @GetMapping("/random")
    public String getRandom(){
        return "redirect:/map?random=true";
    }

    @GetMapping("landmark-details/{id}")
    public String getDetailsForLandmark(@PathVariable Long id){
        return "redirect:/map?details=" + id;
    }

    @GetMapping("/edit-list")
    public String getEditListPage(@RequestParam(required = false) String error, Model model){
        if (error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }

        List<HistoricLandmark> landmarks = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForLandmarks(RequestHelper.createRequestUrl("/landmarks", new HashMap<>())).getBody()));
        List<String> regions = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForStrings(RequestHelper.createRequestUrl("/all-regions", new HashMap<>())).getBody()));
        HashMap<String, String> paramsSorted = new HashMap<>();
        paramsSorted.put("sortedAndCapitalized", "true");
        List<String> classes = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForStrings(RequestHelper.createRequestUrl("/all-historic-classes", paramsSorted)).getBody()));

        model.addAttribute("hasAny", true);
        model.addAttribute("landmarks", landmarks);
        model.addAttribute("regions", regions);
        model.addAttribute("historicClasses", classes);
        model.addAttribute("bodyContent", "edit-list");
        return "master-template";
    }

    @GetMapping("/save-landmark")
    public String getAddLandmarkForm(@RequestParam(required = false) String error, Model model){
        if (error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }

        List<String> classes = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForStrings(RequestHelper.createRequestUrl("/all-historic-classes", new HashMap<>())).getBody()));

        model.addAttribute("add", true);
        model.addAttribute("historicClasses", classes);
        model.addAttribute("bodyContent", "add-landmark");
        return "master-template";
    }

    @GetMapping("/save-landmark/{id}")
    public String getEditLandmarkForm(@PathVariable Long id, @RequestParam(required = false) String error, Model model){

        if (error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }

        List<String> classes = Arrays.asList(Objects.requireNonNull(RequestHelper.sendGetRequestForStrings(RequestHelper.createRequestUrl("/all-historic-classes", new HashMap<>())).getBody()));
        HistoricLandmark landmark = Objects.requireNonNull(RequestHelper.sendGetRequestForLandmark(RequestHelper.createRequestUrl("/landmark/"+id, new HashMap<>())).getBody());

        model.addAttribute("landmark", landmark);
        model.addAttribute("historicClasses", classes);
        model.addAttribute("bodyContent","add-landmark");
        return "master-template";
    }

    @PostMapping("/save-landmark")
    public String addNewOrEditLandmark(@RequestParam(required = false) Long landmarkId,
                              @RequestParam String name,
                              @RequestParam String landmarkClass,
                              @RequestParam Double lat,
                              @RequestParam Double lon,
                              @RequestParam String region,
                              @RequestParam String address,
                              @RequestParam(required = false) String photoUrl){

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("name", name);
        params.add("landmarkClass", landmarkClass);
        params.add("lat", lat);
        params.add("lon", lon);
        params.add("region", region);
        params.add("address", address);
        params.add("photoUrl", photoUrl);

        if (landmarkId!=null){
            String message = RequestHelper.sendPostRequestForString(RequestHelper.createRequestUrl("/edit-landmark/" + landmarkId, new HashMap<>()), new HttpEntity<>(params)).getBody();
            if(message == null) return "redirect:/map/edit-list";
            else return "redirect:/map/save-landmark/" + String.format("%d?error=%s", landmarkId, message);
        }else {
            String message = RequestHelper.sendPostRequestForString(RequestHelper.createRequestUrl("/add-new-landmark", new HashMap<>()), new HttpEntity<>(params)).getBody();
            if(message == null) return "redirect:/map/edit-list";
            else return "redirect:/map/save-landmark?error=" + message;
        }
    }

    @GetMapping("/delete-landmark/{id}")
    public String deleteLandmarkById(@PathVariable Long id){
        RequestHelper.sendDeleteRequestForLandmark(RequestHelper.createRequestUrl("/delete-landmark/" + id, new HashMap<>()));
        return "redirect:/map/edit-list";
    }
}
