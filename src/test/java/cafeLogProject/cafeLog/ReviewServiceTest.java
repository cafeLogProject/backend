package cafeLogProject.cafeLog;

import cafeLogProject.cafeLog.dto.*;
import cafeLogProject.cafeLog.entity.Cafe;
import cafeLogProject.cafeLog.entity.Review;
import cafeLogProject.cafeLog.entity.User;
import cafeLogProject.cafeLog.entity.enums.Tag;
import cafeLogProject.cafeLog.repository.CafeRepository;
import cafeLogProject.cafeLog.repository.ReviewRepository;
import cafeLogProject.cafeLog.repository.UserRepository;
import cafeLogProject.cafeLog.service.Impl.CafeServiceImpl;
import cafeLogProject.cafeLog.service.Impl.ReviewServiceImpl;
import cafeLogProject.cafeLog.service.Impl.UserServiceImpl;
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
                .cafeName(cafe1.getCafename())
                .tags(tags)
                .build();
        //When
        Review review = reviewService.addReview(userA.getId(), reviewRequest);
        //Then
        Assertions.assertEquals(reviewRequest.getCafeId(), reviewService.findReviewById(review.getId()).getCafe().getId());
    }


    public Cafe saveCafe() throws Exception {
        //Given
        RegistCafeRequest registCafeRequest1 =
                RegistCafeRequest.builder()
                        .cafename("카페1")
                        .location("성동구")
                        .avgStar(5.0)
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
                .username("유저A")
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
