package cafeLogProject.cafeLog.service;

import cafeLogProject.cafeLog.api.cafe.dto.SaveCafeReq;
import cafeLogProject.cafeLog.api.cafe.service.CafeService;
import cafeLogProject.cafeLog.api.draftReview.dto.RegistDraftReviewRequest;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.service.DraftReviewService;
import cafeLogProject.cafeLog.api.image.service.ReviewImageService;
import cafeLogProject.cafeLog.api.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.api.review.service.ReviewService;
import cafeLogProject.cafeLog.domains.review.repository.TagRepository;
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

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@Transactional
public class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    DraftReviewService draftReviewService;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ReviewImageService reviewImageService;
    @Autowired
    CafeService cafeService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void before() {
    }

    @AfterEach
    public void after(){

    }

    @Test
    @DisplayName("리뷰 저장 성공")
    public void saveReviewSuccess() {
        // given
        String username = "google-Leeasdf234234";
        String nickname = "nickname";
        User user = User.builder()
                .username(username)
                .nickname(nickname)
                .build();
        User newUser = userRepository.save(user);
        Long userId = newUser.getId();
        // 카페 생성
        String cafeName = "카페1";
        Long cafeId = cafeService.saveCafe(SaveCafeReq.builder()
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
        Long draftReviewId = showDraftReviewResponse.getDraftReviewId();

        // RegistReviewRequest 생성
        String content = "커피가 맛있다~";
        Integer rating = 5;
        LocalDate visitDate = LocalDate.of(2025,2,1);
        LocalDateTime localDateTime = LocalDateTime.now();
        RegistReviewRequest req = RegistReviewRequest.builder()
                .content(content)
                .rating(5)
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
        String username = "google-Leeasdf234234";
        String nickname = "nickname";
        User user = User.builder()
                .username(username)
                .nickname(nickname)
                .build();
        User newUser = userRepository.save(user);
        Long userId = newUser.getId();
        // 카페 생성
        String cafeName = "카페1";
        Long cafeId = cafeService.saveCafe(SaveCafeReq.builder()
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
        Long draftReviewId = showDraftReviewResponse.getDraftReviewId();

        // RegistReviewRequest 생성
        String content = "커피가 맛있다~";
        Integer rating = 5;
        List<Integer> tagIds = Arrays.asList(1,2,3);
        LocalDate visitDate = LocalDate.of(2025,2,1);
        LocalDateTime localDateTime = LocalDateTime.now();
        RegistReviewRequest req = RegistReviewRequest.builder()
                .content(content)
                .rating(5)
                .tagIds(tagIds)
                .visitDate(visitDate)
                .build();

        // when
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
    public void Exception() {

    }

    @Test
    @DisplayName("임시저장 리뷰가 본인 것이 아닌 경우 예외 발생")
    public void invalidDraftReviewException() {

    }


    @Test
    @DisplayName("임시저장 리뷰 존재하지 않는 경우 예외 발생")
    public void notExistDraftReviewException() {

    }



}
