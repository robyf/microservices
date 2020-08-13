package net.robyf.ms.user.persistence;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import net.robyf.ms.user.api.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "users", indexes = {
        @Index(name = "idx_email", columnList = "email", unique = true)
})
public class PersistenceUser {

    @Id
    @GeneratedValue
    private UUID id;

    @Column (name = "email", length = 255, nullable = false)
    private String email;

    @Column (name = "first_name", length = 255, nullable = false)
    private String firstName;

    @Column (name = "last_name", length = 255, nullable = false)
    private String lastName;

    @Column (name = "password", length = 255, nullable = false)
    private String password;

    @Tolerate
    public PersistenceUser() {
    }

    public User asUser() {
        return User.builder()
                .id(this.getId())
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .email(this.getEmail())
                .build();
    }

}
