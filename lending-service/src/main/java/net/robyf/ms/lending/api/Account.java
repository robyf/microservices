package net.robyf.ms.lending.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Account {

    private UUID id;
    private UUID userId;
    private AccountStatus status;
    private BigDecimal balance;

    @Tolerate // NOSONAR
    public Account() { // NOSONAR
    }

}
