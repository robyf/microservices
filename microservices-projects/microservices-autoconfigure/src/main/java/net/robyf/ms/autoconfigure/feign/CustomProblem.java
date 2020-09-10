package net.robyf.ms.autoconfigure.feign;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class CustomProblem {

    String title;
    int status;
    String detail;

    @Tolerate // NOSONAR
    public CustomProblem() { // NOSONAR
    }

}
