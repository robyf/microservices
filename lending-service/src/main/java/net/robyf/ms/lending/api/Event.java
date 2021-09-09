package net.robyf.ms.lending.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Event {

    private UUID id;
    private ZonedDateTime time;
    private int type;
    private BigDecimal amount;
    private BigDecimal resultingBalance;

    @Tolerate // NOSONAR
    public Event() { // NOSONAR
    }

}
