package net.robyf.ms.frontend.graphql;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.GraphQLScalarType;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class TimestampType extends GraphQLScalarType {

    public TimestampType() {
        super("Timestamp", "A timestamp", new Coercing<ZonedDateTime, String>() {

            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");

            @Override
            public String serialize(Object dataFetcherResult) {
                return formatter.format((ZonedDateTime) dataFetcherResult);
            }

            @Override
            public ZonedDateTime parseValue(Object input) {
                if (input instanceof StringValue) {
                    try {
                        return ZonedDateTime.parse(((StringValue) input).getValue(), formatter);
                    } catch(DateTimeParseException dtpe) {
                        throw new CoercingParseValueException("Error parsing date time", dtpe);
                    }
                }
                throw new CoercingParseValueException("Only strings can be parsed to date times");
            }

            @Override
            public ZonedDateTime parseLiteral(Object input) {
                if (input instanceof StringValue) {
                    try {
                        return ZonedDateTime.parse(((StringValue) input).getValue(), formatter);
                    } catch(DateTimeParseException dtpe) {
                        throw new CoercingParseLiteralException("Error parsing date time", dtpe);
                    }
                }
                throw new CoercingParseLiteralException("Only strings can be parsed to date times");
            }

        });
    }

}
