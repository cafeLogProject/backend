package cafeLogProject.cafeLog.service;

import cafeLogProject.cafeLog.api.draftReview.dto.RegistDraftReviewRequest;
import cafeLogProject.cafeLog.api.draftReview.service.DraftReviewService;
import cafeLogProject.cafeLog.api.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.api.review.service.ReviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.yml")
public class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    DraftReviewService draftReviewService;



    @BeforeEach
    public void before() {
        String username = "google-Leeasdf234234";
        Long draftReviewId = (long)1;
        Long cafeId = (long)1;
//        draftReviewService.addDraftReview(username, new RegistDraftReviewRequest(cafeId));
    }

    @AfterEach
    public void after(){

    }

    @Test
    @DisplayName("리뷰 저장 성공")
    public void saveReviewSuccess() {
        // given
        String username = "google-Leeasdf234234";
        Long draftReviewId = (long)1;
//        RegistReviewRequest req = RegistReviewRequest.builder()
//                .content("커피가 맛있다~")
//                .rating(5)
//                .tagIds(Arrays.asList(1,2,3))
//                .visitDate(LocalDate.of(2025,2,1))
//                .build();

        // when
//        Review review = reviewService.addReview(username, draftReviewId,req);
        // then
    }
}
