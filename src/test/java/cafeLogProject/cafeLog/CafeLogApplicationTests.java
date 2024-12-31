package cafeLogProject.cafeLog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class CafeLogApplicationTests {

	private cafeLogProject.cafeLog.api.image.service.imageService imageService;

	@Test
	void contextLoads() {
		System.out.println("안녕");
	}

}

