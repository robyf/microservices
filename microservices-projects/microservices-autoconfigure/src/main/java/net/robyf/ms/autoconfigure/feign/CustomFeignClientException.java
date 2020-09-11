package net.robyf.ms.autoconfigure.feign;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class CustomFeignClientException extends HystrixBadRequestException {

    private final int statusCode;
    private final String title;
    private final String detail;

    public CustomFeignClientException(final CustomProblem problem) {
        super(problem.getTitle());
        this.statusCode = problem.getStatus().getStatusCode();
        this.title = problem.getTitle();
        this.detail = problem.getDetail();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public static CustomFeignClientException decode(final CustomProblem problem) {
        switch (problem.getStatus()) {
            case UNAUTHORIZED:
                return new Unauthorized(problem);
            case FORBIDDEN:
                return new Forbidden(problem);
            case NOT_FOUND:
                return new NotFound(problem);
            default:
                return new CustomFeignClientException(problem);
        }
    }

    public static class Unauthorized extends CustomFeignClientException { // NOSONAR java:S110 better to have inheritance here

        public Unauthorized(final CustomProblem problem) {
            super(problem);
        }

    }

    public static class Forbidden extends CustomFeignClientException { // NOSONAR java:S110 better to have inheritance here

        public Forbidden(final CustomProblem problem) {
            super(problem);
        }

    }

    public static class NotFound extends CustomFeignClientException { // NOSONAR java:S110 better to have inheritance here

        public NotFound(final CustomProblem problem) {
            super(problem);
        }

    }

}
