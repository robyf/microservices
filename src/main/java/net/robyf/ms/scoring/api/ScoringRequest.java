package net.robyf.ms.scoring.api;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class ScoringRequest {

    @NotNull
    @Min(0)
    private BigDecimal income;

    @Tolerate
    public ScoringRequest() {
    }

}
