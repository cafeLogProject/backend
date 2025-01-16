package cafeLogProject.cafeLog.api.cafe.controller;

import cafeLogProject.cafeLog.api.cafe.dto.CafeInfoRes;
import cafeLogProject.cafeLog.api.cafe.dto.IsExistCafeRes;
import cafeLogProject.cafeLog.api.cafe.dto.SaveCafeReq;
import cafeLogProject.cafeLog.api.cafe.dto.SaveCafeRes;
import cafeLogProject.cafeLog.api.cafe.service.CafeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cafes")
@RequiredArgsConstructor
public class CafeController {
    private final CafeService cafeService;

    @GetMapping("/{cafeId}")
    public ResponseEntity<CafeInfoRes> getCafeInfo(@PathVariable Long cafeId) {

        CafeInfoRes cafeInfo = cafeService.getCafeInfo(cafeId);
        return ResponseEntity.ok(cafeInfo);
    }

    @PostMapping("")
    public ResponseEntity<SaveCafeRes> saveCafe(@RequestBody @Valid SaveCafeReq cafeReq) {

        Long cafeId = cafeService.saveCafe(cafeReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SaveCafeRes(cafeId));
    }

    @GetMapping
    public ResponseEntity<IsExistCafeRes> isExistCafe(@RequestParam String name,
                                                      @RequestParam String mapx,
                                                      @RequestParam String mapy) {

        IsExistCafeRes existCafeRes = cafeService.isExistCafe(name, mapx, mapy);
        return ResponseEntity.ok(existCafeRes);
    }

}
