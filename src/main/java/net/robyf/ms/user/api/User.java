package net.robyf.ms.user.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import net.robyf.ms.user.persistence.PersistenceUser;

import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

    @Tolerate // NOSONAR
    public User() { // NOSONAR
    }

}
