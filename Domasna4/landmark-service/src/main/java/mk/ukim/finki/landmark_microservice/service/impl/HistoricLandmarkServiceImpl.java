package mk.ukim.finki.landmark_microservice.service.impl;

import mk.ukim.finki.landmark_microservice.model.HistoricLandmark;
import mk.ukim.finki.landmark_microservice.model.Review;
import mk.ukim.finki.landmark_microservice.model.exception.InvalidInputsException;
import mk.ukim.finki.landmark_microservice.model.exception.InvalidLandmarkIdException;
import mk.ukim.finki.landmark_microservice.repository.HistoricLandmarkRepository;
import mk.ukim.finki.landmark_microservice.service.HistoricLandmarkService;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import jakarta.persistence.Lob;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoricLandmarkServiceImpl implements HistoricLandmarkService {
    private final HistoricLandmarkRepository historicLandmarkRepository;
    private final RestTemplate restTemplate;
    private final static Random random = new Random();

    public HistoricLandmarkServiceImpl(HistoricLandmarkRepository historicLandmarkRepository, RestTemplate restTemplate) {
        this.historicLandmarkRepository = historicLandmarkRepository;
        this.restTemplate = restTemplate;
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
                .map(HistoricLandmark::getHistoricClass).distinct()
                .map(elem -> Arrays.stream(elem.split("_"))
                        .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1))
                        .collect(Collectors.joining(" "))
                ).sorted().toList();
    }

    @Override
    public List<String> findAllHistoricClassesRaw() {
        return this.historicLandmarkRepository.findAll().stream()
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
        if(!urlString.isEmpty()){
            try{
                URI.create(urlString).toURL();
            }catch (Exception exception){
                invalidUrl = true;
            }
        }
        List<String> errors = new ArrayList<>();
        if(lon < -180.0 || lon > 180.0) errors.add("Longitude must be between -180 and 180");
        if(lat < -90.0 || lat > 90.0) errors.add("Latitude must be between -90 and 90");
        if(invalidUrl) errors.add("Invalid Photo URL");

        String message = String.join(", ", errors);
        if(!message.isEmpty()) throw new InvalidInputsException(message);
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
     * A method that filters HistoricLandmarks by parameters
     * @param text - substring of HistoricLandmark.Name
     * @param region - HistoricLandmark.Region
     * @param historicClass - HistoricLandmark.HistoricClass
     * @return A list of HistoricLandmarks filtered by the parameters that aren't null
     */
    @Override
    @Lob
    public List<HistoricLandmark> filterBy(String text, String region, String historicClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder filter = new StringBuilder();
        List<Object> params = new ArrayList<>();
        filter.append("findBy");

        if (text != null && !text.equals("null") && !text.isEmpty()) {
            filter.append("NameContaining");
            params.add(text);
        }
        if (region != null && !region.equals("null") && !region.isEmpty()) {
            if (!params.isEmpty()) filter.append("And");
            filter.append("Region");
            params.add(region);
        }
        if (historicClass != null && !historicClass.equals("null") && !historicClass.isEmpty()) {
            if (!params.isEmpty()) filter.append("And");
            filter.append("HistoricClass");
            params.add(historicClass.replace(" ", "_"));
        }
        if (params.isEmpty()) return this.historicLandmarkRepository.findAll();
        else if (params.size() == 1) filter.append("IgnoreCase");
        else filter.append("AllIgnoreCase");

        java.lang.reflect.Method method;
        Class<HistoricLandmarkRepository> repositoryClass = HistoricLandmarkRepository.class;

        method = repositoryClass.getMethod(filter.toString(), params.stream().map(Object::getClass).toArray(Class<?>[]::new));
        return (List<HistoricLandmark>) method.invoke(this.historicLandmarkRepository, params.toArray());
    }

    /**
     * A method that adds a review to a landmark or edits a review and saves the changes in the landmark
     * @param revId - Review.id
     * @param landmarkId - HistoricLandmark.id
     * @return HistoricLandmark - the landmark that the review was written for
     */
    @Override
    public HistoricLandmark saveReviewToLandmark(Long revId, Long landmarkId) {
        Review review = restTemplate.getForEntity("http://localhost:8080/api/review/"+revId, Review.class).getBody();
        HistoricLandmark landmark = historicLandmarkRepository.findById(landmarkId).orElseThrow(InvalidLandmarkIdException::new);

        if(landmark.getReviews().contains(review)){
            Review rev = landmark.getReviews().stream().filter(r -> r.getId().equals(revId)).findFirst().get();
            rev.setComment(review.getComment());
            rev.setRating(review.getRating());
        }else{
            landmark.getReviews().add(review);
        }
        return historicLandmarkRepository.save(landmark);
    }

    @Override
    public HistoricLandmark deleteReviewFromLandmark(Long revId, Long landmarkId) {
        HistoricLandmark landmark = this.historicLandmarkRepository.findById(landmarkId).orElseThrow(InvalidLandmarkIdException::new);
        landmark.getReviews().removeIf(r -> r.getId().equals(revId));
        return this.historicLandmarkRepository.save(landmark);
    }

}
