package com.asksef;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication(scanBasePackages = "com.asksef")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableSpringDataWebSupport
public class RestAPIPsqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestAPIPsqlApplication.class, args);
    }

}
