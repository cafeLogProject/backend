package cafeLogProject.cafeLog.service.reviewService;

import cafeLogProject.cafeLog.api.cafe.dto.SaveCafeReq;
import cafeLogProject.cafeLog.api.cafe.service.CafeService;
import cafeLogProject.cafeLog.api.draftReview.dto.RegistDraftReviewRequest;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.service.DraftReviewService;
import cafeLogProject.cafeLog.api.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.api.review.dto.UpdateReviewRequest;
import cafeLogProject.cafeLog.api.review.service.ReviewService;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.review.ReviewNotFoundException;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "classpath:application-test.yml")
@ActiveProfiles("test")
@Transactional
public class UpdateReviewTest {
    @Autowired
    ReviewService reviewService;
    @Autowired
    DraftReviewService draftReviewService;
    @Autowired
    CafeService cafeService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRepository reviewRepository;

    String username;
    String nickname;
    Long userId;
    Long cafeId;
    Long draftReviewId;
    Long reviewId;
    List<Integer> tagIds;
    LocalDateTime modifiedAt;

    @BeforeEach
    public void before() {
        // 유저 생성
        username = "google-Leeasdf234234";
        nickname = "nickname";
        User user = User.builder()
                .username(username)
                .nickname(nickname)
                .build();
        User newUser = userRepository.save(user);
        userId = newUser.getId();

        // 카페 생성
        String cafeName = "카페1";
        cafeId = cafeService.saveCafe(SaveCafeReq.builder()
                .title(cafeName)
                .category("카페")
                .roadAddress("도로명주소")
                .mapx("1111")
                .mapy("2222")
                .address("주소주소주소")
                .link("기타링크")
                .build());

        // 임시저장 리뷰 생성
        ShowDraftReviewResponse showDraftReviewResponse = draftReviewService.addDraftReview(username, new RegistDraftReviewRequest(cafeId));
        draftReviewId = showDraftReviewResponse.getDraftReviewId();

        // RegistReviewRequest 생성
        String content = "커피가 맛있다~";
        Integer rating = 5;
        LocalDate visitDate = LocalDate.of(2025,2,1);
        tagIds = Arrays.asList(1,2,3);
        RegistReviewRequest req = RegistReviewRequest.builder()
                .content(content)
                .rating(rating)
                .tagIds(tagIds)
                .visitDate(visitDate)
                .build();

        // 리뷰 저장
        ShowReviewResponse res = reviewService.addReview(username, draftReviewId, req);

        reviewId = res.getReviewId();
        Review review = reviewRepository.findById(reviewId).get();
        modifiedAt = review.getModifiedAt();
    }

    @AfterEach
    public void after(){

    }

    @Test
    @DisplayName("리뷰 수정 성공")
    public void updateReviewSuccess() {
        // given
        String newContent = "맛이 없네";
        Integer newRating = 4;
        LocalDate newVisitDate = LocalDate.of(2025,2,1);
        UpdateReviewRequest req = UpdateReviewRequest.builder()
                .content(newContent)
                .rating(newRating)
                .visitDate(newVisitDate)
                .build();
        
        // when
        ShowReviewResponse res = reviewService.updateReview(username, reviewId, req);

        // then
        Review updatedReview = reviewRepository.findById(reviewId).get();
        assertThat(res.getReviewId()).isEqualTo(reviewId);
        assertThat(res.getContent()).isEqualTo(newContent);
        assertThat(res.getRating()).isEqualTo(newRating);
        assertThat(res.getVisitDate()).isEqualTo(newVisitDate);
        assertThat(res.getImageIds()).isEqualTo(new HashSet<>());
        assertThat(res.getTagIds()).isEqualTo(new HashSet<>(tagIds));
        assertThat(res.getCafeId()).isEqualTo(cafeId);
        assertThat(res.getIsProfileImageExist()).isEqualTo(false);
        assertThat(res.getUserId()).isEqualTo(userId);
        assertThat(res.getNickname()).isEqualTo(nickname);
        assertThat(updatedReview.getModifiedAt()).isAfter(modifiedAt);      // 수정시간 갱신되었는지 확인
    }

    @Test
    @DisplayName("tagIds 수정하는 경우 리뷰 수정 성공")
    public void updateReviewWithUpdateTagSuccess() {
        // given
        String newContent = "맛이 없네";
        Integer newRating = 4;
        LocalDate newVisitDate = LocalDate.of(2025,2,1);

        // when
        List<Integer> newtagIds = Arrays.asList(1,2,3);
        UpdateReviewRequest req = UpdateReviewRequest.builder()
                .content(newContent)
                .rating(newRating)
                .tagIds(newtagIds)
                .visitDate(newVisitDate)
                .build();
        ShowReviewResponse res = reviewService.updateReview(username, reviewId, req);

        // then
        Review updatedReview = reviewRepository.findById(reviewId).get();
        assertThat(res.getReviewId()).isEqualTo(reviewId);
        assertThat(res.getContent()).isEqualTo(newContent);
        assertThat(res.getRating()).isEqualTo(newRating);
        assertThat(res.getVisitDate()).isEqualTo(newVisitDate);
        assertThat(res.getImageIds()).isEqualTo(new HashSet<>());
        assertThat(res.getTagIds()).isEqualTo(new HashSet<>(newtagIds));        // 수정한 태그 같은지 비교
        assertThat(res.getCafeId()).isEqualTo(cafeId);
        assertThat(res.getIsProfileImageExist()).isEqualTo(false);
        assertThat(res.getUserId()).isEqualTo(userId);
        assertThat(res.getNickname()).isEqualTo(nickname);
        assertThat(updatedReview.getModifiedAt()).isAfter(modifiedAt);      
    }

    @Test
    @DisplayName("기존 리뷰의 tagIds 모두 삭제하는 경우 리뷰 수정 성공")
    public void updateReviewWithDeleteTagSuccess() {
        // given
        String newContent = "맛이 없네";
        Integer newRating = 4;
        LocalDate newVisitDate = LocalDate.of(2025,2,1);

        // when
        List<Integer> newtagIds = new ArrayList<>();                    //태그 모두 삭제
        UpdateReviewRequest req = UpdateReviewRequest.builder()
                .content(newContent)
                .rating(newRating)
                .tagIds(newtagIds)
                .visitDate(newVisitDate)
                .build();
        ShowReviewResponse res = reviewService.updateReview(username, reviewId, req);

        // then
        Review updatedReview = reviewRepository.findById(reviewId).get();
        assertThat(res.getReviewId()).isEqualTo(reviewId);
        assertThat(res.getContent()).isEqualTo(newContent);
        assertThat(res.getRating()).isEqualTo(newRating);
        assertThat(res.getVisitDate()).isEqualTo(newVisitDate);
        assertThat(res.getImageIds()).isEqualTo(new HashSet<>());
        assertThat(res.getTagIds()).isEqualTo(new HashSet<>());        // 모두 삭제되었는지 확인
        assertThat(res.getCafeId()).isEqualTo(cafeId);
        assertThat(res.getIsProfileImageExist()).isEqualTo(false);
        assertThat(res.getUserId()).isEqualTo(userId);
        assertThat(res.getNickname()).isEqualTo(nickname);
        assertThat(updatedReview.getModifiedAt()).isAfter(modifiedAt);
    }

    @Test
    @DisplayName("리뷰 존재하지 않는 경우 예외 발생")
    public void notExistReviewException() {
        // given
        String newContent = "맛이 없네";
        Integer newRating = 4;
        LocalDate newVisitDate = LocalDate.of(2025,2,1);
        UpdateReviewRequest req = UpdateReviewRequest.builder()
                .content(newContent)
                .rating(newRating)
                .visitDate(newVisitDate)
                .build();

        // when
        Long invalidReviewId = (long)9999;

        // then
        ReviewNotFoundException e = assertThrows(ReviewNotFoundException.class, ()-> reviewService.updateReview(username, invalidReviewId, req));

        // Then
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.REVIEW_NOT_FOUND_ERROR);
    }

}
