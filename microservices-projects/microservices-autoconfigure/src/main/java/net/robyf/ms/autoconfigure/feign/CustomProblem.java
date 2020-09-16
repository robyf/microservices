package net.robyf.ms.autoconfigure.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomProblem {

    String title;
    int status;
    String detail;

}
