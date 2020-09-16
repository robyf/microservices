package net.robyf.ms.autoconfigure.feign;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomFeignClientExceptionTest {

    @Test
    public void test404() {
        CustomProblem problem = CustomProblem.builder().title("Not found").status(404).detail("Not found detail").build();
        CustomFeignClientException exception = CustomFeignClientException.decode(problem);

        assertThat(exception).isInstanceOf(CustomFeignClientException.NotFound.class);
        assertThat(exception.getTitle()).isEqualTo(problem.getTitle());
        assertThat(exception.getStatusCode()).isEqualTo(problem.getStatus());
        assertThat(exception.getDetail()).isEqualTo(problem.getDetail());
    }

    @Test
    public void test401() {
        CustomProblem problem = CustomProblem.builder().title("Unauthorized").status(401).detail("Unauthorized detail").build();
        CustomFeignClientException exception = CustomFeignClientException.decode(problem);

        assertThat(exception).isInstanceOf(CustomFeignClientException.Unauthorized.class);
        assertThat(exception.getTitle()).isEqualTo(problem.getTitle());
        assertThat(exception.getStatusCode()).isEqualTo(problem.getStatus());
        assertThat(exception.getDetail()).isEqualTo(problem.getDetail());
    }

    @Test
    public void test403() {
        CustomProblem problem = CustomProblem.builder().title("Forbidden").status(403).detail("Forbidden detail").build();
        CustomFeignClientException exception = CustomFeignClientException.decode(problem);

        assertThat(exception).isInstanceOf(CustomFeignClientException.Forbidden.class);
        assertThat(exception.getTitle()).isEqualTo(problem.getTitle());
        assertThat(exception.getStatusCode()).isEqualTo(problem.getStatus());
        assertThat(exception.getDetail()).isEqualTo(problem.getDetail());
    }

    @Test
    public void test400() {
        CustomProblem problem = CustomProblem.builder().title("Bad request").status(400).detail("Bad request detail").build();
        CustomFeignClientException exception = CustomFeignClientException.decode(problem);

        assertThat(exception).isInstanceOf(CustomFeignClientException.class);
        assertThat(exception.getTitle()).isEqualTo(problem.getTitle());
        assertThat(exception.getStatusCode()).isEqualTo(problem.getStatus());
        assertThat(exception.getDetail()).isEqualTo(problem.getDetail());
    }

}
