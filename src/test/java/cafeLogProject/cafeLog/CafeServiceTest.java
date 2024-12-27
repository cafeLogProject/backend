package cafeLogProject.cafeLog;

import cafeLogProject.cafeLog.domains.cafe.dto.RegistCafeRequest;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import cafeLogProject.cafeLog.domains.cafe.service.CafeServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


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
        Cafe cafe = cafeService.addCafe(registCafeRequest1);
        //Then
        System.out.println(cafe.getId());
        Assertions.assertEquals(registCafeRequest1.getCafeName(), cafeService.findCafeById(cafe.getId()).getCafeName());
    }
}
