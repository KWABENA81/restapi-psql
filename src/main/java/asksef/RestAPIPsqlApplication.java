package asksef;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "asksef")
public class RestAPIPsqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestAPIPsqlApplication.class, args);
	}

}
