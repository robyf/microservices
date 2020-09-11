package net.robyf.ms.frontend.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Principal implements Serializable {

    private UUID userId;
    private UUID accountId;
    private UUID sessionId;

}
