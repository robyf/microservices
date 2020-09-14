package net.robyf.ms.autoconfigure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableMicroserviceAutoconfiguration
public class TestApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
