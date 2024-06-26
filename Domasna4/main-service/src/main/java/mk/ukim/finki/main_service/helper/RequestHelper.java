package mk.ukim.finki.main_service.helper;

import mk.ukim.finki.main_service.model.HistoricLandmark;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RequestHelper {
    //Switch url if using docker

    private static String rootUrl = "http://localhost:8081/api";
//    private static String rootUrl = "http://landmark-service:8081/api";
    private static RestTemplate restTemplate;

    private RequestHelper(RestTemplate restTemplate) {
        RequestHelper.restTemplate = restTemplate;
    }

    public static String createRequestUrl(String endpoint, Map<String, String> queryParams) {
        String query = "";
        if (!queryParams.isEmpty()) {
            query = queryParams.entrySet()
                    .stream()
                    .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                    .collect(
                            Collectors.joining("&"));
        }
        return String.format("%s%s?%s", rootUrl, endpoint, query);
    }

    public static ResponseEntity<HistoricLandmark[]> sendGetRequestForLandmarks(String requestUrl) {
        return restTemplate.getForEntity(requestUrl, HistoricLandmark[].class);
    }

    public static ResponseEntity<String[]> sendGetRequestForStrings(String requestUrl) {
        return restTemplate.getForEntity(requestUrl, String[].class);
    }

    public static ResponseEntity<String> sendPostRequestForString(String requestUrl, HttpEntity<MultiValueMap<String, Object>> request) {
        return restTemplate.postForEntity(requestUrl, request, String.class);
    }

    public static HistoricLandmark sendPostRequestForLandmark(String requestUrl, HttpEntity<MultiValueMap<String, Object>> request) {
        return restTemplate.postForEntity(requestUrl, request, HistoricLandmark.class).getBody();
    }

    public static ResponseEntity<HistoricLandmark> sendGetRequestForLandmark(String requestUrl) {
        return restTemplate.getForEntity(requestUrl, HistoricLandmark.class);
    }

    public static void sendDeleteRequestForLandmark(String requestUrl) {
        restTemplate.getForEntity(requestUrl, void.class);
    }
}
