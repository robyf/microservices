package net.robyf.ms.autoconfigure.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnClass(Docket.class)
public class SwaggerConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.version}")
    private String appVersion;

    @Value("${spring.application.title}")
    private String appTitle;

    @Value("${spring.application.description}")
    private String appDescription;

    @Bean
    public Docket currentApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName(appName)
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, defaultMessages())
                .globalResponses(HttpMethod.POST, defaultMessages())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.robyf.ms"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(appTitle)
                .description(appDescription)
                .version(appVersion).build();
    }

    private List<Response> defaultMessages() {
        List<Response> responses = new ArrayList<>();
        responses.add(new ResponseBuilder()
                .code("500")
                .description("500 series: Internal server error. Returns a valid application/problem+json object.")
                .build());
        responses.add(new ResponseBuilder()
                .code("400")
                .description("400 series: Client error (validation, auth). Returns a valid application/problem+json object.")
                .build());
        return responses;
    }

}
