package net.robyf.ms.frontend.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;

import java.io.IOException;

@Component
@Slf4j
public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        log.info("response headers {}", response.headers());
        if (response.headers().get("content-type").contains("application/problem+json")) {
            log.info("feign error with problem {}", methodKey);
            if (isClientErrorStatus(response.status())) {
                log.info("client error");

                try {
                    CustomProblem problem = objectMapper.createParser(response.body().asInputStream()).readValueAs(CustomProblem.class);
                    log.info("parsed problem {}", problem);
                    return CustomFeignClientException.decode(problem);
                } catch (IOException ioe) {
                    log.error("Error parsing response problem", ioe);
                }
            }
        }
        return this.defaultDecoder.decode(methodKey, response);
    }

    private static boolean isClientErrorStatus(final int status) {
        return status >= 400 && status < 500;
    }

}
