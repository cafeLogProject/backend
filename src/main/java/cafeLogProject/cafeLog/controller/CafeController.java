package cafeLogProject.cafeLog.controller;

import cafeLogProject.cafeLog.dto.RegistCafeRequest;
import cafeLogProject.cafeLog.service.CafeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cafes")
@RequiredArgsConstructor
public class CafeController {
    private final CafeService cafeService;

    @PostMapping("/")
    public ResponseEntity<?> registCafe(@RequestBody @Valid RegistCafeRequest registCafeRequest) {
        // dto에 필수 입력란 @NotNull 설정 필요, @Valid 이용하여 필수 입력란 누락시 자동으로 400 error 리턴하도록 함
        cafeService.addCafe(registCafeRequest);
        return ResponseEntity.ok().body(null);
    }
}
