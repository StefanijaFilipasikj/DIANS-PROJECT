package mk.ukim.finki.historicLandmarks.service.impl;


import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.repository.HistoricLandmarkRepository;
import mk.ukim.finki.historicLandmarks.service.HistoricLandmarkService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class HistoricLandmarkServiceImpl implements HistoricLandmarkService {

    private final HistoricLandmarkRepository historicLandmarkRepository;

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
                if (data.length == 6){
                    HistoricLandmark hl = new HistoricLandmark();
                    hl.setLat(Double.parseDouble(data[0]));
                    hl.setLon(Double.parseDouble(data[1]));
                    hl.setHistoricClass(data[2]);
                    hl.setName(data[3]);
                    hl.setAddress(data[4]);
                    hl.setRegion(data[5]);

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
    public List<String> findAllRegions() {
        return historicLandmarkRepository.findAll().stream().map(HistoricLandmark::getRegion).distinct().toList();
    }

    @Override
    public List<String> findAllHistoricClass() {
        return historicLandmarkRepository.findAll().stream().map(HistoricLandmark::getHistoricClass).distinct().toList();
    }

    @Override
    public List<HistoricLandmark> searchByName(String text) {
        return historicLandmarkRepository.findAll().stream().filter(h -> h.getName().toLowerCase()
                .contains(text.toLowerCase())).sorted().toList();
    }


}
