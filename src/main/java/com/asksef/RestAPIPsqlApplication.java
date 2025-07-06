package com.asksef;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.asksef")
public class RestAPIPsqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestAPIPsqlApplication.class, args);
	}

}
