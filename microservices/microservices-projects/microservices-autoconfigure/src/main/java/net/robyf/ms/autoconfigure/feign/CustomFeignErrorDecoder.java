package net.robyf.ms.autoconfigure.feign;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ConditionalOnClass(Feign.class)
@Slf4j
public class CustomFeignErrorDecoder implements ErrorDecoder {

    private ErrorDecoder defaultDecoder = new Default();

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        log.debug("response headers {}", response.headers());
        if (response.headers().get("content-type").contains("application/problem+json")) {
            log.debug("feign error with problem {}", methodKey);
            if (isClientErrorStatus(response.status())) {
                log.debug("client error");
                log.debug("objectMapper {}", objectMapper);
                try (JsonParser parser = objectMapper.createParser(response.body().asInputStream())) {
                    CustomProblem problem = parser.readValueAs(CustomProblem.class);
                    log.debug("parsed problem {}", problem);
                    return CustomFeignClientException.decode(problem);
                } catch (IOException ioe) {
                    log.error("Error parsing response problem", ioe);
                }
            }
        }
        return this.defaultDecoder.decode(methodKey, response);
    }

    /**
     * For unit tests only.
     */
    void setDefaultDecoder(final ErrorDecoder decoder) {
        this.defaultDecoder = decoder;
    }

    /**
     * For unit tests only.
     */
    void resetDefaultDecoder() {
        this.defaultDecoder = new Default();
    }

    static boolean isClientErrorStatus(final int status) {
        return status >= 400 && status < 500;
    }

}
