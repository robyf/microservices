package net.robyf.ms.frontend.client;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class CustomFeignClientException extends HystrixBadRequestException {

    private final int statusCode;
    private final String title;
    private final String detail;

    public CustomFeignClientException(final CustomProblem problem) {
        super(problem.getTitle());
        this.statusCode = problem.getStatus();
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
            case 401:
                return new CustomFeignClientException.Unauthorized(problem);
            case 403:
                return new CustomFeignClientException.Forbidden(problem);
            case 404:
                return new CustomFeignClientException.NotFound(problem);
            default:
                return new CustomFeignClientException(problem);
        }
    }

    public static class Unauthorized extends CustomFeignClientException {

        public Unauthorized(final CustomProblem problem) {
            super(problem);
        }

    }

    public static class Forbidden extends CustomFeignClientException {

        public Forbidden(final CustomProblem problem) {
            super(problem);
        }

    }

    public static class NotFound extends CustomFeignClientException {

        public NotFound(final CustomProblem problem) {
            super(problem);
        }

    }

}
