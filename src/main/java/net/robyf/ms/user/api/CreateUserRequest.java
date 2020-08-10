package net.robyf.ms.user.api;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateUserRequest {

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @Tolerate
    public CreateUserRequest() {
    }

}
