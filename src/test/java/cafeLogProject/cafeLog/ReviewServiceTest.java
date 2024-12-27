package cafeLogProject.cafeLog;

import cafeLogProject.cafeLog.domains.cafe.dto.RegistCafeRequest;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.dto.TagCategory;
import cafeLogProject.cafeLog.domains.review.dto.TagDto;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.review.domain.enums.Tag;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import cafeLogProject.cafeLog.domains.review.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import cafeLogProject.cafeLog.domains.cafe.service.CafeServiceImpl;
import cafeLogProject.cafeLog.domains.review.service.ReviewServiceImpl;
import cafeLogProject.cafeLog.domains.user.dto.RegistUserRequest;
import cafeLogProject.cafeLog.domains.user.service.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class ReviewServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    CafeRepository cafeRepository;
    @Autowired
    CafeServiceImpl cafeService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReviewServiceImpl reviewService;

    @BeforeEach
    public void before(){
        System.out.println("Test Before");
    }

    @AfterEach
    public void after(){
        System.out.println("Test After");
    }

    @Test
    @DisplayName("리뷰 찾지못함 에러 출력")
    public void reviewNotFoundExceptionTest() {
        reviewService.findReviewById(1);
    }

    @Test
    @DisplayName("리뷰 저장")
    public void saveReview() throws Exception {
        //Given
        Cafe cafe1 = saveCafe();
        System.out.println("saveCafe()");
        User userA = saveUser();
        System.out.println("saveUser()");
        List<String> images = new ArrayList<>();
        images.add("이미지url1");
        images.add("이미지url2");
        List<TagDto> menuTags = new ArrayList<>();
        List<TagDto> interiorTags = new ArrayList<>();
        menuTags.add(new TagDto(Tag.MENU_SELF_ROASTING.getNum(), Tag.MENU_SELF_ROASTING.getDescription()));
        menuTags.add(new TagDto(Tag.MENU_HAND_DRIP.getNum(), Tag.MENU_HAND_DRIP.getDescription()));
        interiorTags.add(new TagDto(Tag.ETC_CLEAN_TOILET.getNum(), Tag.ETC_CLEAN_TOILET.getDescription()));
        interiorTags.add(new TagDto(Tag.ETC_HAVE_OUTSIDE.getNum(), Tag.ETC_HAVE_OUTSIDE.getDescription()));
        TagCategory tags = new TagCategory(menuTags, interiorTags);
        RegistReviewRequest reviewRequest = RegistReviewRequest.builder()
                .content("내용A")
                .rating(4)
                .visitDate(LocalDate.now())
                .images(images)
                .cafeId(cafe1.getId())
                .cafeName(cafe1.getCafeName())
                .tags(tags)
                .build();
        //When
        Review review = reviewService.addReview(userA.getId(), reviewRequest);
        //Then
        Assertions.assertEquals(reviewRequest.getCafeId(), reviewService.findReviewById(review.getId()).getCafe().getId());
    }


    public Cafe saveCafe() throws Exception {
        //Given
        List<Long> locationXY = new ArrayList<>();
        locationXY.add((long)13412341);
        locationXY.add((long)56875687);
        RegistCafeRequest registCafeRequest1 =
                RegistCafeRequest.builder()
                        .cafeName("카페1")
                        .locationStr("성동구")
                        .locationXY(locationXY)
                        .isClosedDown(false)
                        .build();
        //When
        Cafe cafe1 = cafeService.addCafe(registCafeRequest1);
        //Then
        return cafe1;
    }

    public User saveUser() throws Exception {
        //Given
        RegistUserRequest registUserRequest = RegistUserRequest.builder()
                .userName("유저A")
                .profileImage("이미지A")
                .nickname("닉네임A")
                .introduce("소개A")
                .email("이메일A")
                .provider("providerA")
                .build();
        //When
        User user =  userService.addUser(registUserRequest);
        //Then
        return user;
    }
}
