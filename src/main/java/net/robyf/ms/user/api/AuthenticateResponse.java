package net.robyf.ms.user.api;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AuthenticateResponse {

    @NotNull
    @NotEmpty
    @Email
    private AuthenticateStatus status;

    private User user;

    @Tolerate
    public AuthenticateResponse() {
    }

}
