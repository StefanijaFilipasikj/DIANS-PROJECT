package mk.ukim.finki.historicLandmarks.service.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.historicLandmarks.model.HistoricLandmark;
import mk.ukim.finki.historicLandmarks.model.enumerations.Role;
import mk.ukim.finki.historicLandmarks.repository.HistoricLandmarkRepository;
import mk.ukim.finki.historicLandmarks.repository.UserRepository;
import mk.ukim.finki.historicLandmarks.service.DataService;
import mk.ukim.finki.historicLandmarks.service.UserService;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class DataServiceImpl implements DataService {
    private final HistoricLandmarkRepository historicLandmarkRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public DataServiceImpl(HistoricLandmarkRepository historicLandmarkRepository, UserRepository userRepository, UserService userService) {
        this.historicLandmarkRepository = historicLandmarkRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void addInitialData(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/filteredData.csv"));
            String line;
            br.readLine(); //skip header
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
            this.userService.register("admin", "admin", "admin", "Admin", "Admin", "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg", Role.ROLE_ADMIN);
            this.userService.register("user", "user", "user", "User", "User", "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg", Role.ROLE_USER);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteAllData() {
        this.historicLandmarkRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Override
    public boolean isEmpty() {
        return this.historicLandmarkRepository.count() == 0 && this.userRepository.count() == 0;
    }
}
