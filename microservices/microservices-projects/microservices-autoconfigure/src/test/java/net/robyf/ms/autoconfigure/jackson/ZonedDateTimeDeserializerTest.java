package net.robyf.ms.autoconfigure.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ZonedDateTimeDeserializerTest {

    private final ZonedDateTimeDeserializer deserializer = new ZonedDateTimeDeserializer();

    @Test (expected = JsonMappingException.class)
    public void testDeserializeMissingToken() throws Exception {
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.hasToken(JsonToken.VALUE_STRING)).thenReturn(false);

        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Mockito.when(context.wrongTokenException(Mockito.isA(JsonParser.class), Mockito.eq(String.class), Mockito.eq(JsonToken.VALUE_STRING), Mockito.eq("Expected string.")))
                .thenReturn(new JsonMappingException(null, "Expected string."));

        deserializer.deserialize(parser, context);
    }

    @Test
    public void testDeserializeEmptyToken() throws Exception {
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
        Mockito.when(parser.getText()).thenReturn("");

        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        ZonedDateTime result = deserializer.deserialize(parser, context);

        assertThat(result).isNull();
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
        Mockito.when(parser.getText()).thenReturn("2020-09-08T19:15:45.000000+03");

        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        ZonedDateTime result = deserializer.deserialize(parser, context);

        assertThat(result).isEqualTo(ZonedDateTime.of(2020, 9, 8, 19, 15, 45, 0, ZoneId.of("Europe/Helsinki")));
    }

}
