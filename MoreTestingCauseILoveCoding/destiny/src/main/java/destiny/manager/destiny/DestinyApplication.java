package destiny.manager.destiny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("destiny.manager.destiny")
public class DestinyApplication {

	public static void main(String[] args) {
		SpringApplication.run(DestinyApplication.class, args);
	}
}
