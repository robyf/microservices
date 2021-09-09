package net.robyf.ms.lending.api;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class CreateCreditDecisionRequest {

    @NotNull
    @Min(0)
    private BigDecimal income;

    @Tolerate // NOSONAR
    public CreateCreditDecisionRequest() { // NOSONAR
    }

}
