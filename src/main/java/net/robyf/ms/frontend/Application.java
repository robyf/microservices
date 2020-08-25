package net.robyf.ms.frontend;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.zalando.problem.ProblemModule;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.ZonedDateTime;

@SpringBootApplication
@EnableSwagger2
@EnableCircuitBreaker
@EnableFeignClients
public class Application {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper().registerModule(new ProblemModule().withStackTraces());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        objectMapper.registerModule(module);

        return objectMapper;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
