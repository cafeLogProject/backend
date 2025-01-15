package cafeLogProject.cafeLog.test.controller;

import cafeLogProject.cafeLog.common.auth.oauth2.CustomOAuth2User;
import cafeLogProject.cafeLog.test.dto.ReviewFindRes;
import cafeLogProject.cafeLog.test.dto.ReviewSaveReq;
import cafeLogProject.cafeLog.test.dto.ReviewSaveRes;
import cafeLogProject.cafeLog.test.service.ReviewEntityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test/review")
@RequiredArgsConstructor
public class ReviewControllerV2 {

    private final ReviewEntityService reviewEntityService;

    @PostMapping
    public ResponseEntity<ReviewSaveRes> saveReview(@AuthenticationPrincipal CustomOAuth2User user,
                                                    @RequestBody @Valid ReviewSaveReq reviewSaveReq) {

        ReviewSaveRes reviewSaveRes = reviewEntityService.saveReview(user.getName(), reviewSaveReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewSaveRes);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReviewFindRes>> findAllReview() {

        List<ReviewFindRes> allReview = reviewEntityService.findAllReview();
        return ResponseEntity.ok(allReview);
    }

    @GetMapping("/all/tag")
    public ResponseEntity<List<ReviewFindRes>> findAllByTagId(@RequestParam List<Integer> tags) {

        List<ReviewFindRes> allByTagIds = reviewEntityService.findAllByTagIds(tags);
        return ResponseEntity.ok(allByTagIds);
    }


}
