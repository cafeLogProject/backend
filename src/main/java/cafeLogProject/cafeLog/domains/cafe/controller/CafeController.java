package cafeLogProject.cafeLog.domains.cafe.controller;

import cafeLogProject.cafeLog.domains.cafe.dto.RegistCafeRequest;
import cafeLogProject.cafeLog.domains.cafe.service.CafeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cafes")
@RequiredArgsConstructor
public class CafeController {
    private final CafeService cafeService;

    @PostMapping("/")
    public ResponseEntity<?> registCafe(@RequestBody @Valid RegistCafeRequest registCafeRequest) {
        cafeService.addCafe(registCafeRequest);
        return ResponseEntity.ok().body(null);
    }



}
