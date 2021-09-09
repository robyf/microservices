package net.robyf.ms.autoconfigure.feign;

import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext
public class CustomFeignErrorDecoderTest {

    @Autowired
    private CustomFeignErrorDecoder decoder;

    @After
    public void reset() {
        decoder.resetDefaultDecoder();
    }

    @Test
    public void testIsClientErrorStatus() {
        assertThat(CustomFeignErrorDecoder.isClientErrorStatus(200)).isFalse();
        assertThat(CustomFeignErrorDecoder.isClientErrorStatus(302)).isFalse();
        assertThat(CustomFeignErrorDecoder.isClientErrorStatus(404)).isTrue();
        assertThat(CustomFeignErrorDecoder.isClientErrorStatus(502)).isFalse();
    }

    @Test
    public void testNotAProblem() {
        ErrorDecoder defaultDecoder = Mockito.mock(ErrorDecoder.class);
        Mockito.when(defaultDecoder.decode(Mockito.anyString(), Mockito.any())).thenReturn(new Exception());

        Request request = Request.create(Request.HttpMethod.GET, "url", Collections.emptyMap(), new byte[]{}, Charset.defaultCharset(), new RequestTemplate());
        Response response = Response.builder()
                .request(request)
                .headers(Collections.singletonMap("content-type", Collections.singletonList("application/json")))
                .build();

        decoder.setDefaultDecoder(defaultDecoder);

        decoder.decode("method", response);

        Mockito.verify(defaultDecoder, Mockito.times(1)).decode(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void testNotAClientError() {
        ErrorDecoder defaultDecoder = Mockito.mock(ErrorDecoder.class);
        Mockito.when(defaultDecoder.decode(Mockito.anyString(), Mockito.any())).thenReturn(new Exception());

        Request request = Request.create(Request.HttpMethod.GET, "url", Collections.emptyMap(), new byte[]{}, Charset.defaultCharset(), new RequestTemplate());
        Response response = Response.builder()
                .request(request)
                .headers(Collections.singletonMap("content-type", Collections.singletonList("application/problem+json")))
                .status(502)
                .build();

        decoder.setDefaultDecoder(defaultDecoder);

        decoder.decode("method", response);

        Mockito.verify(defaultDecoder, Mockito.times(1)).decode(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void testClientError() {
        ErrorDecoder defaultDecoder = Mockito.mock(ErrorDecoder.class);
        Mockito.when(defaultDecoder.decode(Mockito.anyString(), Mockito.any())).thenReturn(new Exception());

        String problem = "{\"title\":\"Not found\",\"status\":404,\"detail\":\"Not found\"}";

        Request request = Request.create(Request.HttpMethod.GET, "url", Collections.emptyMap(), new byte[]{}, Charset.defaultCharset(), new RequestTemplate());
        Response response = Response.builder()
                .request(request)
                .headers(Collections.singletonMap("content-type", Collections.singletonList("application/problem+json")))
                .status(404)
                .body(problem, Charset.defaultCharset())
                .build();

        decoder.setDefaultDecoder(defaultDecoder);

        assertThat(decoder.decode("method", response)).isInstanceOf(CustomFeignClientException.NotFound.class);

        Mockito.verify(defaultDecoder, Mockito.times(0)).decode(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void testExceptionParsingResponseDoesNotPreventDecoding() throws Exception {
        ErrorDecoder defaultDecoder = Mockito.mock(ErrorDecoder.class);
        Mockito.when(defaultDecoder.decode(Mockito.anyString(), Mockito.any())).thenReturn(new Exception());

        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.read()).thenThrow(new IOException());

        Request request = Request.create(Request.HttpMethod.GET, "url", Collections.emptyMap(), new byte[]{}, Charset.defaultCharset(), new RequestTemplate());
        Response response = Response.builder()
                .request(request)
                .headers(Collections.singletonMap("content-type", Collections.singletonList("application/problem+json")))
                .status(400)
                .body(inputStream, 20)
                .build();

        decoder.setDefaultDecoder(defaultDecoder);

        decoder.decode("method", response);

        Mockito.verify(defaultDecoder, Mockito.times(1)).decode(Mockito.anyString(), Mockito.any());
    }

}
