package cafeLogProject.cafeLog.service.draftReviewService;

import cafeLogProject.cafeLog.api.cafe.controller.NaverApiController;
import cafeLogProject.cafeLog.api.cafe.dto.SaveCafeReq;
import cafeLogProject.cafeLog.api.cafe.service.CafeService;
import cafeLogProject.cafeLog.api.cafe.service.NaverApiService;
import cafeLogProject.cafeLog.api.draftReview.dto.RegistDraftReviewRequest;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.service.DraftReviewService;
import cafeLogProject.cafeLog.api.image.service.DraftReviewImageService;
import cafeLogProject.cafeLog.api.image.service.ImageUtil;
import cafeLogProject.cafeLog.common.auth.exception.UserNotAuthenticatedException;
import cafeLogProject.cafeLog.common.auth.jwt.JWTUtil;
import cafeLogProject.cafeLog.common.config.RedisConfig;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.draftReview.DraftReviewNotFoundException;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@Transactional
public class DeleteDraftReviewsAndImagesTest {
    @Autowired
    DraftReviewService draftReviewService;
    @Autowired
    CafeService cafeService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DraftReviewImageService draftReviewImageService;

    String username;
    Long userId;
    Long cafeId;
    Long draftReviewId1;
    Long draftReviewId2;
    Long reviewId;
    List<Integer> tagIds;
    LocalDateTime modifiedAt;
    MockMultipartFile imageFile;

    @BeforeEach
    public void before() throws Exception{
        // 유저 생성
        username = "google-Leeasdf234234";
        User user = User.builder()
                .username(username)
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
        ShowDraftReviewResponse showDraftReviewResponse1 = draftReviewService.addDraftReview(username, new RegistDraftReviewRequest(cafeId));
        draftReviewId1 = showDraftReviewResponse1.getDraftReviewId();
        ShowDraftReviewResponse showDraftReviewResponse2 = draftReviewService.addDraftReview(username, new RegistDraftReviewRequest(cafeId));
        draftReviewId2 = showDraftReviewResponse2.getDraftReviewId();

        // 저장할 이미지 mock 제작
        imageFile = new MockMultipartFile("test", "test".getBytes());

        // 이미지 저장
        // 리뷰 1개당 이미지 2개씩 저장
        // ImageUtil.saveImage 함수가 static void함수이므로 MockedStatic 사용하여 mocking 진행
        try (MockedStatic<ImageUtil> imageUtil = mockStatic(ImageUtil.class)) {
            imageUtil.when(()-> ImageUtil.saveImage(anyString(), anyString(), any(MultipartFile.class))).thenAnswer(i -> {
                System.out.println("모킹된 ImageUtil.save() 호출");
                return null;
            });
            draftReviewImageService.addDraftReviewImage(username, draftReviewId1, imageFile);
            draftReviewImageService.addDraftReviewImage(username, draftReviewId1, imageFile);
            draftReviewImageService.addDraftReviewImage(username, draftReviewId2, imageFile);
            draftReviewImageService.addDraftReviewImage(username, draftReviewId2, imageFile);
        }
    }

    @AfterEach
    public void after(){
    }

    @Test
    @DisplayName("여러 임시저장 리뷰에 이미지 존재시 모두 삭제 성공")
    public void deleteDraftReviewsAndImagesWithImagesSuccess() {
        // when
        List<Long> draftReviewIds = List.of(draftReviewId1, draftReviewId2);

        // then
        draftReviewService.deleteDraftReviewsAndImages(username, draftReviewIds);

        // db 삭제 여부 확인
        DraftReviewNotFoundException e1 = assertThrows(DraftReviewNotFoundException.class, ()-> draftReviewService.findDraftReview(username, draftReviewId1));
        assertThat(e1.getErrorCode()).isEqualTo(ErrorCode.DRAFT_REVIEW_NOT_FOUND_ERROR);
        DraftReviewNotFoundException e2 = assertThrows(DraftReviewNotFoundException.class, ()-> draftReviewService.findDraftReview(username, draftReviewId2));
        assertThat(e2.getErrorCode()).isEqualTo(ErrorCode.DRAFT_REVIEW_NOT_FOUND_ERROR);
    }

    @Test
    @DisplayName("이미지 삭제 실패해도 로그만 남기고 성공 처리하는지 확인")
    public void Success() {
        // when
        List<Long> draftReviewIds = List.of(draftReviewId1, draftReviewId2);

        // then
        assertDoesNotThrow(() -> draftReviewService.deleteDraftReviewsAndImages(username, draftReviewIds));

        // db 삭제 여부 확인
        DraftReviewNotFoundException e1 = assertThrows(DraftReviewNotFoundException.class, ()-> draftReviewService.findDraftReview(username, draftReviewId1));
        assertThat(e1.getErrorCode()).isEqualTo(ErrorCode.DRAFT_REVIEW_NOT_FOUND_ERROR);
        DraftReviewNotFoundException e2 = assertThrows(DraftReviewNotFoundException.class, ()-> draftReviewService.findDraftReview(username, draftReviewId2));
        assertThat(e2.getErrorCode()).isEqualTo(ErrorCode.DRAFT_REVIEW_NOT_FOUND_ERROR);
    }

    @Test
    @DisplayName("삭제하려는 임시저장 리뷰 중 존재하지 않는 리뷰Id가 포함된 경우 예외 발생")
    public void InvalidDraftReviewException() {
        // when
        // 잘못된 리뷰id로 삭제 요청
        List<Long> draftReviewIds = List.of(draftReviewId1, (long)9999, draftReviewId2);

        // then
        DraftReviewNotFoundException e = assertThrows(DraftReviewNotFoundException.class, ()-> draftReviewService.deleteDraftReviewsAndImages(username, draftReviewIds));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DRAFT_REVIEW_NOT_FOUND_ERROR);

        // 롤백 성공 여부 확인
        assertDoesNotThrow(() -> draftReviewService.findDraftReview(username, draftReviewId1));
        assertDoesNotThrow(() -> draftReviewService.findDraftReview(username, draftReviewId2));
    }

    @Test
    @DisplayName("삭제하려는 임시저장 리뷰 중 본인의 리뷰가 아닌 것이 포함된 경우 예외 발생")
    public void InvalidUserException() {
        // given
        // 또다른 유저 생성
        String username2 = "naver-easdf789789";
        User user2 = User.builder()
                .username(username2)
                .build();
        userRepository.save(user2);
        // 또다른 유저의 임시저장 리뷰 생성
        ShowDraftReviewResponse showDraftReviewResponse1 = draftReviewService.addDraftReview(username2, new RegistDraftReviewRequest(cafeId));
        Long user2draftReviewId = showDraftReviewResponse1.getDraftReviewId();

        // when
        // 본인의 것이 아닌 리뷰 삭제 요청
        List<Long> draftReviewIds = List.of(draftReviewId1, user2draftReviewId, draftReviewId2);

        // then
        UserNotAuthenticatedException e = assertThrows(UserNotAuthenticatedException.class, ()-> draftReviewService.deleteDraftReviewsAndImages(username2, draftReviewIds));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_AUTH_ERROR);

        // 롤백 성공 여부 확인
        assertDoesNotThrow(() -> draftReviewService.findDraftReview(username, draftReviewId1));
        assertDoesNotThrow(() -> draftReviewService.findDraftReview(username2, user2draftReviewId));
        assertDoesNotThrow(() -> draftReviewService.findDraftReview(username, draftReviewId2));

    }
}
