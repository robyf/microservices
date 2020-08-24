package net.robyf.ms.frontend.graphql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import net.robyf.ms.frontend.client.CustomFeignClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientException extends RuntimeException implements GraphQLError {

    private final int statusCode;
    private final String title;
    private final String detail;

    public ClientException(final CustomFeignClientException fce) {
        super(fce.getMessage());
        this.statusCode = fce.getStatusCode();
        this.title = fce.getTitle();
        this.detail = fce.getDetail();
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.ValidationError;
    }

    @Override
    public Map<String, Object> getExtensions() {
        Map<String, Object> ext = new HashMap<>();
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
