package net.robyf.ms.frontend.graphql;

import graphql.language.StringValue;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TimestampTypeTest {

    private final TimestampType type = new TimestampType();

    @Test
    public void testSerialize() throws Exception {
        ZonedDateTime zdt = ZonedDateTime.of(2020, 12, 15, 14, 00, 00, 0, ZoneId.of("Europe/Helsinki"));

        String serialized = (String) type.getCoercing().serialize(zdt);

        assertThat(serialized).isEqualTo("2020-12-15T14:00:00.000000+02:00");
    }

    @Test
    public void testParseValue() throws Exception {
        ZonedDateTime result = (ZonedDateTime) type.getCoercing().parseValue(new StringValue("2020-09-08T12:30:00.000000+03:00"));
        assertThat(result).isEqualTo(ZonedDateTime.of(2020, 9, 8, 12, 30, 0, 0, ZoneId.of("Europe/Helsinki")));
    }

    @Test (expected = CoercingParseValueException.class)
    public void testParseValueMalformed() throws Exception {
        type.getCoercing().parseValue(new StringValue("not a date"));
    }

    @Test (expected = CoercingParseValueException.class)
    public void testParseValueNotStringValue() throws Exception {
        type.getCoercing().parseValue("a string");
    }

    @Test
    public void testParseLiteral() throws Exception {
        ZonedDateTime result = (ZonedDateTime) type.getCoercing().parseLiteral(new StringValue("2020-09-08T12:30:00.000000+03:00"));
        assertThat(result).isEqualTo(ZonedDateTime.of(2020, 9, 8, 12, 30, 0, 0, ZoneId.of("Europe/Helsinki")));
    }

    @Test (expected = CoercingParseLiteralException.class)
    public void testParseLiteralMalformed() throws Exception {
        type.getCoercing().parseLiteral(new StringValue("not a date"));
    }

    @Test (expected = CoercingParseLiteralException.class)
    public void testParseLiteralNotStringValue() throws Exception {
        type.getCoercing().parseLiteral("a string");
    }

}
