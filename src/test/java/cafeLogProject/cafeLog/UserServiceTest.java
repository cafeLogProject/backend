package cafeLogProject.cafeLog;

import cafeLogProject.cafeLog.domains.user.dto.RegistUserRequest;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import cafeLogProject.cafeLog.domains.user.service.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class UserServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserServiceImpl userService;


    @BeforeEach
    public void before(){
        System.out.println("Test Before");
    }

    @AfterEach
    public void after(){
        System.out.println("Test After");
    }



    @Test
    @DisplayName("유저 저장")
    public void saveUser() throws Exception {
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
        System.out.println(user.getEmail());
        Assertions.assertEquals(registUserRequest.getEmail(), userService.findUserById(user.getId()).getEmail());
//        return user;
    }

}
