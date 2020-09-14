package net.robyf.ms.frontend;

import net.robyf.ms.autoconfigure.ExceptionHandling;
import net.robyf.ms.autoconfigure.MicroserviceConfiguration;
import net.robyf.ms.autoconfigure.feign.CustomFeignErrorDecoder;
import net.robyf.ms.autoconfigure.security.PrincipalHelper;
import net.robyf.ms.autoconfigure.swagger.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableFeignClients
@Import({
        ExceptionHandling.class,
        SwaggerConfiguration.class,
        CustomFeignErrorDecoder.class,
        MicroserviceConfiguration.class,
        PrincipalHelper.class
})
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
