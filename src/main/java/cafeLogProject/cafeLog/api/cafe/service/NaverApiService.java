package cafeLogProject.cafeLog.api.cafe.service;

import cafeLogProject.cafeLog.api.cafe.dto.NaverApiRes;
import cafeLogProject.cafeLog.api.cafe.dto.NaverCafeInfo;
import cafeLogProject.cafeLog.common.exception.cafe.CafeApiParsingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static cafeLogProject.cafeLog.common.exception.ErrorCode.CAFE_API_PARSING_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverApiService {

    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    public NaverApiRes searchNaverApi(String name) {

        String response = sendRequestAndGetResponse(name);

        NaverApiRes naverApiRes = new NaverApiRes(extractCafeInfo(response));

        return naverApiRes;
    }

    private String sendRequestAndGetResponse(String name) {

        String url = "https://openapi.naver.com/v1/search/local.json?query=" + name + "&display=5";

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", naverClientId);
        headers.add("X-Naver-Client-Secret", naverClientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (responseEntity.getStatusCode().is5xxServerError()) {
            log.error("Naver API SERVER ERROR");
            throw new CafeApiParsingException(CAFE_API_PARSING_ERROR);
        }

        String response = responseEntity.getBody();
        return response;
    }

    private List<NaverCafeInfo> extractCafeInfo(String response) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response);
            JsonNode items = jsonResponse.get("items");

            List<NaverCafeInfo> filteredCafes = new ArrayList<>();
            for (JsonNode item : items) {
                String category = item.get("category").asText();
                if (category.contains("카페")) {
                    NaverCafeInfo cafe = new NaverCafeInfo();
                    cafe.setTitle(item.get("title").asText());
                    cafe.setLink(item.get("link").asText());
                    cafe.setCategory(category);
                    cafe.setAddress(item.get("address").asText());
                    cafe.setRoadAddress(item.get("roadAddress").asText());
                    cafe.setMapx(item.get("mapx").asText());
                    cafe.setMapy(item.get("mapy").asText());
                    filteredCafes.add(cafe);
                }
            }
            return filteredCafes;

        } catch (Exception e) {
            throw new CafeApiParsingException(CAFE_API_PARSING_ERROR);
        }
    }

}
