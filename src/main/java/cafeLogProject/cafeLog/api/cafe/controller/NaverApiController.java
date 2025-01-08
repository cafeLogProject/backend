package cafeLogProject.cafeLog.api.cafe.controller;

import cafeLogProject.cafeLog.api.cafe.dto.NaverApiRes;
import cafeLogProject.cafeLog.api.cafe.service.NaverApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NaverApiController {

    private final NaverApiService naverApiService;

    @GetMapping("/api/cafes/search")
    public ResponseEntity<NaverApiRes> searchNaverAPI(@RequestParam String name) {

        NaverApiRes naverApiRes = naverApiService.searchNaverApi(name);
        return ResponseEntity.ok(naverApiRes);
    }


}
