package net.robyf.ms.frontend;

import net.robyf.ms.autoconfigure.ExceptionHandling;
import net.robyf.ms.autoconfigure.MicroserviceConfiguration;
import net.robyf.ms.autoconfigure.swagger.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableCircuitBreaker
@EnableFeignClients
@Import({
        ExceptionHandling.class,
        SwaggerConfiguration.class,
        MicroserviceConfiguration.class
})
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
