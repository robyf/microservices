package net.robyf.ms.autoconfigure.feign;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.zalando.problem.Status;

@Data
@Builder
public class CustomProblem {

    String title;
    Status status;
    String detail;

    @Tolerate // NOSONAR
    public CustomProblem() { // NOSONAR
    }

}
