package net.robyf.ms.frontend;

import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * A configuration class that enables cookie support in TestRestTemplate (needed to properly test login and logout).
 */
@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    public TestRestTemplate testRestTemplate(ApplicationContext applicationContext) {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .errorHandler(new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(ClientHttpResponse response) throws IOException {
                        return false;
                    }

                    @Override
                    public void handleError(ClientHttpResponse response) throws IOException {

                    }
                });

        TestRestTemplate testRestTemplate =
                new TestRestTemplate(restTemplateBuilder, null, null, TestRestTemplate.HttpClientOption.ENABLE_REDIRECTS, TestRestTemplate.HttpClientOption.ENABLE_COOKIES);

        // let this testRestTemplate resolve paths relative to http://localhost:${local.server.port}
        LocalHostUriTemplateHandler handler =
                new LocalHostUriTemplateHandler(applicationContext.getEnvironment(), "http");
        testRestTemplate.setUriTemplateHandler(handler);

        return testRestTemplate;
    }

}
