package cafeLogProject.cafeLog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CafeLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeLogApplication.class, args);
	}

}
