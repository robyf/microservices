package net.robyf.ms.frontend.graphql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import net.robyf.ms.frontend.client.CustomFeignClientException;
import org.zalando.problem.DefaultProblem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientException extends RuntimeException implements GraphQLError {

    private final int statusCode;
    private final String title;
    private final String detail;
    private final String type;
    private final ErrorType errorType;

    public ClientException(final CustomFeignClientException fce) {
        super(fce.getMessage());
        this.statusCode = fce.getStatusCode();
        this.title = fce.getTitle();
        this.detail = fce.getDetail();
        this.type = "feign";
        this.errorType = ErrorType.DataFetchingException;
    }

    public ClientException(final DefaultProblem problem) {
        super(problem.getMessage());
        this.statusCode = problem.getStatus().getStatusCode();
        this.title = problem.getTitle();
        this.detail = problem.getDetail();
        this.type = "problem";
        this.errorType = ErrorType.ValidationError;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return Collections.emptyList();
    }

    @Override
    public ErrorType getErrorType() {
        return this.errorType;
    }

    @Override
    public Map<String, Object> getExtensions() {
        Map<String, Object> ext = new HashMap<>();
        ext.put("type", this.type);
        ext.put("statusCode", this.statusCode);
        ext.put("title", this.title);
        ext.put("detail", this.detail);
        return ext;
    }

    @Override
    @JsonIgnore
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

}
