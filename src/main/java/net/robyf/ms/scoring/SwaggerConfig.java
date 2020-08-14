package net.robyf.ms.scoring;

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
        return new Docket(DocumentationType.SWAGGER_2).groupName("scoring")
                .useDefaultResponseMessages(false)
//                .globalResponses(HttpMethod.GET, defaultMessages())
//                .globalResponses(HttpMethod.POST, defaultMessages())
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.ant("/scoring-service/v1/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Scoring service")
                .description("Determines if users can be granted a loan or not.")
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
