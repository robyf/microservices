package net.robyf.ms.autoconfigure.security;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class Principal implements Serializable {

    private String jwt;
    private UUID userId;
    private UUID accountId;

    @Tolerate // NOSONAR
    public Principal() { // NOSONAR
    }

}
