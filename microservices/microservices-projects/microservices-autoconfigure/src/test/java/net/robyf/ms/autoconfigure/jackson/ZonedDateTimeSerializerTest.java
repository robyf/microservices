package net.robyf.ms.autoconfigure.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ZonedDateTimeSerializerTest {

    private final ZonedDateTimeSerializer serializer = new ZonedDateTimeSerializer();

    @Test
    public void testSerialize() throws Exception {
        ZonedDateTime zdt = ZonedDateTime.of(2020, 9, 8, 7, 10, 0, 0, ZoneId.of("Europe/Helsinki"));

        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Mockito.doNothing().when(generator).writeString(Mockito.anyString());

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        serializer.serialize(zdt, generator, null);

        Mockito.verify(generator, Mockito.times(1)).writeString(captor.capture());
        assertThat(captor.getValue()).isEqualTo("2020-09-08T07:10:00.000000+03");
    }

}
