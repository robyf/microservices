package net.robyf.ms.frontend.graphql;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.GraphQLErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zalando.problem.DefaultProblem;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ErrorHandler implements GraphQLErrorHandler {

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        return errors.stream().map(this::getNested).collect(Collectors.toList());
    }

    private GraphQLError getNested(GraphQLError error) {
        log.info("type {}", error.getClass().getName());
        if (error instanceof ExceptionWhileDataFetching) {
            ExceptionWhileDataFetching exceptionError = (ExceptionWhileDataFetching) error;
            log.info("type exception {}", exceptionError.getException().getClass().getName());
            if (exceptionError.getException() instanceof GraphQLError) {
                return (GraphQLError) exceptionError.getException();
            }
            if (exceptionError.getException() instanceof DefaultProblem) {
                return new ClientException((DefaultProblem) exceptionError.getException());
            }
        }
        return error;
    }

}
