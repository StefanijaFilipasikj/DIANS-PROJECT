package mk.ukim.finki.main_service.helper;

import mk.ukim.finki.main_service.model.HistoricLandmark;
import mk.ukim.finki.main_service.model.Review;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.CacheRequest;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RequestHelper {
    private static String rootUrl = "http://localhost:8081/api";

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

    public static HttpEntity<MultiValueMap<String, Object>> buildPostRequestParams(Object... objects) {
        MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
        for (int i = 0; i < objects.length; i += 2) {
            requestParams.add(objects[i].toString(), objects[i + 1].toString().replace("\"", ""));
        }
        return new HttpEntity<>(requestParams, new HttpHeaders());
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

//    public static void sendSaveRequestForReview(String requestUrl, HttpEntity<MultiValueMap<String, Object>> request) {
//        restTemplate.postForEntity(requestUrl, request, String.class);
//    }
//
//    public static ResponseEntity<Review> sendGetRequestForReview(String requestUrl) {
//        return restTemplate.getForEntity(requestUrl, Review.class);
//    }


//    public static ResponseEntity<Player[]> sendGerRequestForFilterPlayers(String url) {
//        return restTemplate.getForEntity(
//                url,
//                Player[].class
//        );
//    }

//    public static void sendPostForCreatePlayer(String url, HttpEntity<MultiValueMap<String, Object>> request) {
//        restTemplate.postForEntity(url, request, void.class);
//    }
}
