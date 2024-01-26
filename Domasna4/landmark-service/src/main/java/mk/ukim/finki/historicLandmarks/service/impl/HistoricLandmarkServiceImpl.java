package mk.ukim.finki.historicLandmarks.service.impl;


import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.model.exception.InvalidInputsException;
import mk.ukim.finki.historicLandmarks.model.exception.InvalidLandmarkIdException;
import mk.ukim.finki.historicLandmarks.repository.HistoricLandmarkRepository;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoricLandmarkServiceImpl implements HistoricLandmarkService {

    private final HistoricLandmarkRepository historicLandmarkRepository;
    private final static Random random = new Random();

    public HistoricLandmarkServiceImpl(HistoricLandmarkRepository historicLandmarkRepository) {
        this.historicLandmarkRepository = historicLandmarkRepository;
    }
    @Override
    public List<HistoricLandmark> findAllLandmarks() {
        return historicLandmarkRepository.findAll().stream()
                .sorted(Comparator.comparing(HistoricLandmark::getRating).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public HistoricLandmark findLandmarkById(Long id) {
        return historicLandmarkRepository.findById(id).orElseThrow(InvalidLandmarkIdException::new);
    }

    @Override
    public List<String> findAllRegions() {
        return historicLandmarkRepository.findAll().stream().map(HistoricLandmark::getRegion).distinct().sorted().toList();
    }

    @Override
    public List<String> findAllHistoricClassesCapitalizedAndSorted() {
        return historicLandmarkRepository.findAll().stream()
                .map(HistoricLandmark::getHistoricClass)
                .distinct()
                .map(elem -> Arrays.stream(elem.split("_"))
                        .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1))
                        .collect(Collectors.joining(" "))
                )
                .sorted()
                .toList();
    }

    @Override
    public List<String> findAllHistoricClassesRaw() {
        return  this.historicLandmarkRepository.findAll().stream()
                .map(HistoricLandmark::getHistoricClass)
                .distinct().toList();
    }

    @Override
    public void addNewLandmark(String name, String landmarkClass, Double lat, Double lon, String region, String address, String photoUrl){
        checkInputs(photoUrl, lat, lon);
        this.historicLandmarkRepository.save(new HistoricLandmark(lat,lon,landmarkClass,name,address,region,photoUrl));
    }

    @Override
    public void editLandmarkById(Long landmarkId, String name, String landmarkClass, Double lat, Double lon, String region, String address, String photoUrl){
        HistoricLandmark landmark = this.historicLandmarkRepository.findById(landmarkId).orElseThrow(InvalidLandmarkIdException::new);
        checkInputs(photoUrl, lat, lon);

        landmark.setName(name);
        landmark.setHistoricClass(landmarkClass);
        landmark.setLon(lon);
        landmark.setLat(lat);
        landmark.setAddress(address);
        landmark.setRegion(region);
        landmark.setPhotoUrl(photoUrl);
        this.historicLandmarkRepository.save(landmark);
    }

    private void checkInputs(String urlString, Double lat, Double lon){
        boolean invalidUrl = false;
        try{
            URI.create(urlString).toURL();
        }catch (Exception exception){
            invalidUrl = true;
        }

        if(invalidUrl && (lon < -180.0 || lon > 180.0) && (lat < -90.0 || lat > 90.0))
            throw new InvalidInputsException("Longitude must be between -180 and 180, Latitude must be between -90 and 90, Invalid URL");
        else if((lon < -180.0 || lon > 180.0) && (lat < -90.0 || lat > 90.0))
            throw new InvalidInputsException("Longitude must be between -180 and 180, Latitude must be between -90 and 90");
        else if(lon < -180.0 || lon > 180.0)
            throw new InvalidInputsException("Longitude must be between -180 and 180");
        else if(lat < -90.0 || lat > 90.0)
            throw new InvalidInputsException("Latitude must be between -90 and 90");
        else if(invalidUrl)
            throw  new InvalidInputsException("Invalid URL");
    }

    @Override
    public void deleteLandmarkById(Long id) {
        historicLandmarkRepository.deleteById(id);
    }

    @Override
    public List<HistoricLandmark> findTop10Landmarks() {
        return this.historicLandmarkRepository.findAll().stream()
                .sorted(Comparator.comparing(HistoricLandmark::getRating).reversed())
                .limit(10).collect(Collectors.toList());
    }

    @Override
    public HistoricLandmark findRandomLandmark() {
        int index = random.nextInt((int)this.historicLandmarkRepository.count());
        return this.historicLandmarkRepository.findAll().get(index);
    }

    /**
     * A method that filters HistoricLandmarks by three parameters
     * @param text - substring of HistoricLandmark.Name
     * @param region - HistoricLandmark.Region
     * @param historicClass - HistoricLandmark.HistoricClass
     * @return A list of HistoricLandmarks filtered by the parameters that aren't null
     */
    @Override
    public List<HistoricLandmark> filterBy(String text, String region, String historicClass) {

        if(historicClass != null) historicClass = historicClass.replace(" ", "_");

        if(text != null && !text.isEmpty() && region != null && !region.isEmpty() && historicClass != null && !historicClass.isEmpty()){
            return this.historicLandmarkRepository.findByNameContainingIgnoreCaseAndRegionIgnoreCaseAndHistoricClassIgnoreCase(text, region, historicClass);
        }else if(text != null && !text.isEmpty() && region != null && !region.isEmpty()){
            return this.historicLandmarkRepository.findByNameContainingIgnoreCaseAndRegionIgnoreCase(text, region);
        }else if(text != null && !text.isEmpty() && historicClass != null && !historicClass.isEmpty()){
            return this.historicLandmarkRepository.findByNameContainingIgnoreCaseAndHistoricClassIgnoreCase(text, historicClass);
        }else if(region != null && !region.isEmpty() && historicClass != null && !historicClass.isEmpty()){
            return this.historicLandmarkRepository.findByRegionIgnoreCaseAndHistoricClassIgnoreCase(region, historicClass);
        } else if(text != null && !text.isEmpty()){
            return this.historicLandmarkRepository.findByNameContainingIgnoreCase(text);
        }else if(region != null && !region.isEmpty()){
            return this.historicLandmarkRepository.findByRegionIgnoreCase(region);
        }else if(historicClass != null && !historicClass.isEmpty()){
            return this.historicLandmarkRepository.findByHistoricClassIgnoreCase(historicClass);
        }else{
            return this.historicLandmarkRepository.findAll();
        }
    }
}
