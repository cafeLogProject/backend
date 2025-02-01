package cafeLogProject.cafeLog.service.reviewService;

import cafeLogProject.cafeLog.api.cafe.dto.SaveCafeReq;
import cafeLogProject.cafeLog.api.cafe.service.CafeService;
import cafeLogProject.cafeLog.api.draftReview.dto.RegistDraftReviewRequest;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.service.DraftReviewService;
import cafeLogProject.cafeLog.api.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.api.review.service.ReviewService;
import cafeLogProject.cafeLog.common.auth.exception.UserNotAuthenticatedException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.draftReview.DraftReviewNotFoundException;
import cafeLogProject.cafeLog.common.exception.review.TagInvalidException;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@Transactional
public class SaveReviewTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    DraftReviewService draftReviewService;
    @Autowired
    CafeService cafeService;
    @Autowired
    UserRepository userRepository;

    String username;
    String nickname;
    Long userId;
    Long cafeId;
    Long draftReviewId;

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
    }

    @AfterEach
    public void after(){

    }

    @Test
    @DisplayName("리뷰 저장 성공")
    public void saveReviewSuccess() {
        // given
        // RegistReviewRequest 생성
        String content = "커피가 맛있다~";
        Integer rating = 5;
        LocalDate visitDate = LocalDate.of(2025,2,1);
        LocalDateTime localDateTime = LocalDateTime.now();
        RegistReviewRequest req = RegistReviewRequest.builder()
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .build();

        // when
        ShowReviewResponse res = reviewService.addReview(username, draftReviewId, req);


        // then
        assertThat(res.getContent()).isEqualTo(content);
        assertThat(res.getRating()).isEqualTo(rating);
        assertThat(res.getTagIds()).isEqualTo(new HashSet<>());
        assertThat(res.getVisitDate()).isEqualTo(visitDate);
        assertThat(res.getImageIds()).isEqualTo(new HashSet<>());
        assertThat(res.getIsProfileImageExist()).isEqualTo(false);
        assertThat(res.getUserId()).isEqualTo(userId);
        assertThat(res.getNickname()).isEqualTo(nickname);
        assertThat(res.getCreatedAt()).isAfter(localDateTime);
        assertThat(res.getCafeId()).isEqualTo(cafeId);
    }

    @Test
    @DisplayName("tagIds 존재하는 경우 리뷰 저장 성공")
    public void saveReviewWithTagsSuccess() {
        // given
        // RegistReviewRequest 생성
        String content = "커피가 맛있다~";
        Integer rating = 5;
        LocalDate visitDate = LocalDate.of(2025,2,1);
        LocalDateTime localDateTime = LocalDateTime.now();

        // when
        List<Integer> tagIds = Arrays.asList(1,2,3);
        RegistReviewRequest req = RegistReviewRequest.builder()
                .content(content)
                .rating(rating)
                .tagIds(tagIds)
                .visitDate(visitDate)
                .build();
        ShowReviewResponse res = reviewService.addReview(username, draftReviewId, req);

        // then
        assertThat(res.getContent()).isEqualTo(content);
        assertThat(res.getRating()).isEqualTo(rating);
        assertThat(res.getTagIds()).isEqualTo(new HashSet<>(tagIds));
        assertThat(res.getVisitDate()).isEqualTo(visitDate);
        assertThat(res.getImageIds()).isEqualTo(new HashSet<>());
        assertThat(res.getIsProfileImageExist()).isEqualTo(false);
        assertThat(res.getUserId()).isEqualTo(userId);
        assertThat(res.getNickname()).isEqualTo(nickname);
        assertThat(res.getCreatedAt()).isAfter(localDateTime);
        assertThat(res.getCafeId()).isEqualTo(cafeId);
    }

    @Test
    @DisplayName("reviewImageIds 존재하는 경우(임시저장 리뷰에 이미지 저장한 경우) 이미지 폴더 이동 여부 검사, 리뷰 저장 성공 여부 검사")
    public void saveReviewWithImagesSuccess() {

    }

    @Test
    @DisplayName("태그 id가 유효하지 않는 경우 예외 발생")
    public void invalidTagIdException() {
        // given
        // RegistReviewRequest 생성
        String content = "커피가 맛있다~";
        Integer rating = 5;
        LocalDate visitDate = LocalDate.of(2025,2,1);

        // when
        List<Integer> tagIds = Arrays.asList(9999,8888);                        //정의되지 않은 태그 입력
        RegistReviewRequest req = RegistReviewRequest.builder()
                .content(content)
                .rating(rating)
                .tagIds(tagIds)
                .visitDate(visitDate)
                .build();

        // then
        TagInvalidException e = assertThrows(TagInvalidException.class, ()-> reviewService.addReview(username, draftReviewId, req));

        // Then
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.TAG_INVALID_ERROR);
    }

    @Test
    @DisplayName("임시저장 리뷰가 본인 것이 아닌 경우 예외 발생")
    public void invalidDraftReviewException() {
        // given
        // RegistReviewRequest 생성
        String content = "커피가 맛있다~";
        Integer rating = 5;
        LocalDate visitDate = LocalDate.of(2025,2,1);
        RegistReviewRequest req = RegistReviewRequest.builder()
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .build();

        // when
        // 새로운 유저 생성
        String username2 = "naver-asdfasdf890890";
        String nickname2 = "nickname";
        User user2 = User.builder()
                .username(username2)
                .nickname(nickname2)
                .build();
        User newUser2 = userRepository.save(user2);
        Long userId2 = newUser2.getId();

        // then
        // 새로운 유저가 기존 유저의 draftReview로 리뷰 등록시 에러 리턴
        UserNotAuthenticatedException e = assertThrows(UserNotAuthenticatedException.class, ()-> reviewService.addReview(username2, draftReviewId, req));

        // Then
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_AUTH_ERROR);
    }
    
    @Test
    @DisplayName("임시저장 리뷰 존재하지 않는 경우 예외 발생")
    public void notExistDraftReviewException() {
        // given
        // RegistReviewRequest 생성
        String content = "커피가 맛있다~";
        Integer rating = 5;
        LocalDate visitDate = LocalDate.of(2025,2,1);
        RegistReviewRequest req = RegistReviewRequest.builder()
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .build();

        // when
        // 존재하지 않는 draftReviewId 입력
        Long draftReviewId = (long)9999;

        // then
        DraftReviewNotFoundException e = assertThrows(DraftReviewNotFoundException.class, ()-> reviewService.addReview(username, draftReviewId, req));

        // Then
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DRAFT_REVIEW_NOT_FOUND_ERROR);
    }
}
