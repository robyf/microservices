package net.robyf.ms.frontend.security;

import lombok.Data;
import lombok.Builder;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class Principal implements Serializable {

    private UUID userId;
    private UUID accountId;

    @Tolerate // NOSONAR
    public Principal() { // NOSONAR
    }

}
