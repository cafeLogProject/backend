package cafeLogProject.cafeLog.service.reviewImageService;


import cafeLogProject.cafeLog.api.cafe.dto.SaveCafeReq;
import cafeLogProject.cafeLog.api.cafe.service.CafeService;
import cafeLogProject.cafeLog.api.draftReview.dto.RegistDraftReviewRequest;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.service.DraftReviewService;
import cafeLogProject.cafeLog.api.image.dto.RegistReviewImageResponse;
import cafeLogProject.cafeLog.api.image.service.ImageUtil;
import cafeLogProject.cafeLog.api.image.service.ReviewImageService;
import cafeLogProject.cafeLog.api.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.api.review.service.ReviewService;
import cafeLogProject.cafeLog.common.auth.exception.UserNotAuthenticatedException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.review.ReviewNotFoundException;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.image.repository.ReviewImageRepository;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@Transactional
public class AddReviewImageTest {
    @Autowired
    ReviewImageService reviewImageService;

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
    @Autowired
    ReviewImageRepository reviewImageRepository;

    String username;
    String nickname;
    Long userId;
    Long cafeId;
    Long draftReviewId;
    Long reviewId;
    List<Integer> tagIds;
    LocalDateTime modifiedAt;
    MockMultipartFile imageFile;

    @BeforeEach
    public void before() throws Exception{
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

        // 저장할 이미지
        imageFile = new MockMultipartFile("test", "test".getBytes());
    }

    @AfterEach
    public void after(){
    }

    @Test
    @DisplayName("리뷰 이미지 파일 저장 성공")
    public void addReviewImageSuccess() {
        //given
        // ImageUtil.saveImage 함수가 static void함수이므로 MockedStatic 사용하여 mocking 진행
        try (MockedStatic<ImageUtil> imageUtil = mockStatic(ImageUtil.class)) {
            imageUtil.when(()-> ImageUtil.saveImage(anyString(), anyString(), any(MultipartFile.class))).thenAnswer(i -> {
                System.out.println("모킹된 ImageUtil.save() 호출");
                return null;
            });

            // when
            RegistReviewImageResponse res = reviewImageService.addReviewImage(username, reviewId, imageFile);

            // then
            UUID newImageId = UUID.fromString(res.getImageId());
            Review review = reviewRepository.findById(reviewId).get();
            ReviewImage reviewImage = reviewImageRepository.findById(newImageId).get();

            assertThat(newImageId).isEqualTo(reviewImage.getId());
            // Review의 imageIds 필드에 추가되었는지 확인
            assertThat(review).isEqualTo(reviewImage.getReview());
        }
    }

    @Test
    @DisplayName("리뷰가 본인 것이 아닌 경우 예외 발생")
    public void invalidReviewException() {
        // when
        // 새로운 유저 등록
        String username2 = "naver-asdfasdf890890";
        String nickname2 = "nickname";
        User user2 = User.builder()
                .username(username2)
                .nickname(nickname2)
                .build();
        User newUser2 = userRepository.save(user2);
        Long userId2 = newUser2.getId();
        
        // 새로운 유저가 기존 유저의 review로 이미지 등록시 에러 리턴
        UserNotAuthenticatedException e = assertThrows(UserNotAuthenticatedException.class, ()-> reviewImageService.addReviewImage(username2, reviewId, imageFile));

        // Then
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_AUTH_ERROR);
    }

    @Test
    @DisplayName("리뷰가 존재하지 않는 경우 예외 발생")
    public void notExistReviewException() {
        // when
        // 존재하지 않는 draftReviewId 입력
        Long invalidReviewId = (long)9999;
        ReviewNotFoundException e = assertThrows(ReviewNotFoundException.class, ()-> reviewImageService.addReviewImage(username, invalidReviewId, imageFile));

        // then
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.REVIEW_NOT_FOUND_ERROR);
    }



}
