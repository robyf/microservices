package net.robyf.ms.frontend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {


    @Value("${spring.application.version}")
    private String appVersion;

    @Bean
    public Docket currentApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("frontend")
                .useDefaultResponseMessages(false)
//                .globalResponses(HttpMethod.GET, defaultMessages())
//                .globalResponses(HttpMethod.POST, defaultMessages())
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.ant("/api/v1/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Frontend service")
                .description("Provides services to the frontend app.")
                .version(appVersion).build();
    }
/*
    private List<Response> defaultMessages() {
        return Lists.newArrayList(
                new ResponseBuilder().code("500").description("500 series: Internal server error")
                        .build(),
                new ResponseBuilder().code("400")
                        .description("400 series: Client error (validation, auth)").build());
    }
*/
}
