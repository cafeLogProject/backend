package cafeLogProject.cafeLog;

import cafeLogProject.cafeLog.dto.RegistCafeRequest;
import cafeLogProject.cafeLog.entity.Cafe;
import cafeLogProject.cafeLog.repository.CafeRepository;
import cafeLogProject.cafeLog.service.Impl.CafeServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class CafeServiceTest {

    @Autowired
    CafeRepository cafeRepository;
    @Autowired
    CafeServiceImpl cafeService;

    @BeforeEach
    public void before(){
        System.out.println("Test Before");
    }

    @AfterEach
    public void after(){
        System.out.println("Test After");
    }

    @Test
    @DisplayName("카페 저장")
    public void saveCafe() throws Exception {
        //Given
        RegistCafeRequest registCafeRequest1 =
                RegistCafeRequest.builder()
                        .cafename("카페1")
                        .location("성동구")
                        .avgStar(5.0)
                        .isClosedDown(false)
                    .build();
        //When
        Cafe cafe = cafeService.addCafe(registCafeRequest1);
        //Then
        System.out.println(cafe.getId());
        Assertions.assertEquals(registCafeRequest1.getCafename(), cafeService.findCafeById(cafe.getId()).getCafename());
    }
}
