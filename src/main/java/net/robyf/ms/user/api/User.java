package net.robyf.ms.user.api;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import net.robyf.ms.user.persistence.PersistenceUser;

import java.util.UUID;

@Data
@Builder
public class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

    @Tolerate
    public User() {
    }

    public static User build(final PersistenceUser pUser) {
        return User.builder()
                .id(pUser.getId())
                .firstName(pUser.getFirstName())
                .lastName(pUser.getLastName())
                .email(pUser.getEmail())
                .build();
    }

}
