package net.robyf.ms.frontend.security;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class Principal implements Serializable {

    private UUID userId;
    private UUID accountId;
    private UUID sessionId;

    @Tolerate // NOSONAR
    public Principal() { // NOSONAR
    }

}
