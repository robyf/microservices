package net.robyf.ms.lending.api;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class MonetaryTransactionResponse {

    @NotNull
    @Min(0)
    private BigDecimal resultingBalance;

    @Tolerate // NOSONAR
    public MonetaryTransactionResponse() { // NOSONAR
    }

}
