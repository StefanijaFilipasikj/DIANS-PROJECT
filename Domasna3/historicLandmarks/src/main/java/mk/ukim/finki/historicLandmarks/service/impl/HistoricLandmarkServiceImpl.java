package mk.ukim.finki.historicLandmarks.service.impl;


import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.model.Review;
import mk.ukim.finki.historicLandmarks.model.User;
import mk.ukim.finki.historicLandmarks.repository.HistoricLandmarkRepository;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HistoricLandmarkServiceImpl implements HistoricLandmarkService {

    private final HistoricLandmarkRepository historicLandmarkRepository;
    private static Random random = new Random();

    public HistoricLandmarkServiceImpl(HistoricLandmarkRepository historicLandmarkRepository) {
        this.historicLandmarkRepository = historicLandmarkRepository;
    }

    @Override
    public void saveData(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/filteredData.csv"));
            String line;
            String header = br.readLine(); //skip header
            while ((line = br.readLine())!=null){
                String [] data = line.split(",",-1);
                if (data.length == 7){
                    HistoricLandmark hl = new HistoricLandmark();
                    hl.setLat(Double.parseDouble(data[0]));
                    hl.setLon(Double.parseDouble(data[1]));
                    hl.setHistoricClass(data[2]);
                    hl.setName(data[3]);
                    hl.setAddress(data[4]);
                    hl.setRegion(data[5]);
                    hl.setPhotoUrl(data[6]);
                    historicLandmarkRepository.save(hl);
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllData() {
        historicLandmarkRepository.deleteAll();
    }

    @Override
    public List<HistoricLandmark> findAll() {
        return historicLandmarkRepository.findAll().stream().sorted().toList();
    }

    @Override
    public Optional<HistoricLandmark> findById(Long id) {
        return historicLandmarkRepository.findById(id);
    }

    @Override
    public List<String> findAllRegions() {
        return historicLandmarkRepository.findAll().stream().map(HistoricLandmark::getRegion).distinct().sorted().toList();
    }

    @Override
    public List<String> findAllHistoricClass() {
        List<String> a = historicLandmarkRepository.findAll().stream()
                .map(HistoricLandmark::getHistoricClass)
                .distinct().toList();
        return capitalize(a);
    }

    @Override
    public List<String> findAllHistoricClassRaw() {
        List<String> a = historicLandmarkRepository.findAll().stream()
                .map(HistoricLandmark::getHistoricClass)
                .distinct().toList();
        return a;
    }

    @Override
    public List<String> capitalize(List<String> list) {
        return list.stream().map(elem -> {
            String[] parts = elem.split("_");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
            }
            return String.join(" ", parts);
        }).sorted().toList();
    }

    @Override
    public String removeCapitalize(String s) {
        String[] parts = s.split(" ");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].substring(0, 1).toLowerCase() + parts[i].substring(1);
        }
        return String.join("_", parts);
    }

    @Override
    public List<HistoricLandmark> searchByName(String text) {
        return historicLandmarkRepository.findAll().stream().filter(h -> h.getName().toLowerCase()
                .contains(text.toLowerCase())).sorted().toList();
    }

    @Override
    public Optional<HistoricLandmark> edit(String landmarkId, String name, String landmarkClass, String lat, String lon, String address, String region, String photoUrl) {
        return historicLandmarkRepository.editLandmarkById(landmarkId,name,landmarkClass,lat,lon,address,region,photoUrl);
    }

    @Override
    public Optional<HistoricLandmark> save(String lat, String lon, String landmarkClass, String name, String address, String region, String photoUrl) {
        return Optional.of(historicLandmarkRepository.save(new HistoricLandmark(Double.parseDouble(lat),Double.parseDouble(lon),landmarkClass,name,address,region,photoUrl)));
    }

    @Override
    public void delete(Long id) {
        historicLandmarkRepository.deleteById(id);
    }

    @Override
    public List<HistoricLandmark> findTop10() {
        return this.historicLandmarkRepository.findAll().stream()
                .sorted(Comparator.comparing(HistoricLandmark::getRating).reversed())
                .limit(10).collect(Collectors.toList());
    }

    @Override
    public HistoricLandmark findRandomLandmark() {
        int index = random.nextInt((int)this.historicLandmarkRepository.count());
        return this.historicLandmarkRepository.findAll().get(index);
    }

    @Override
    public boolean empty() {
        return this.historicLandmarkRepository.count() == 0;
    }
}
