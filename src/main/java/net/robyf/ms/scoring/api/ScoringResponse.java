package net.robyf.ms.scoring.api;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ScoringResponse {

    @NotNull
    @NotEmpty
    private UUID scoringId;

    @NotNull
    @Min(0)
    @Max(1)
    private BigDecimal probabilityOfDefault;

    @Tolerate
    public ScoringResponse() {
    }

}
