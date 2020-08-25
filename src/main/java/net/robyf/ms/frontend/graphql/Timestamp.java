package net.robyf.ms.frontend.graphql;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Timestamp extends GraphQLScalarType {

    public Timestamp() {
        super("Timestamp", "A timestamp", new Coercing<ZonedDateTime, String>() {

            @Override
            public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
                return formatter.format((ZonedDateTime) dataFetcherResult);
            }

            @Override
            public ZonedDateTime parseValue(Object input) throws CoercingParseValueException {
                // TODO parse
                return null;
            }

            @Override
            public ZonedDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
                // TODO parse
                return null;
            }

        });
    }

}
