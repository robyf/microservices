package net.robyf.ms.frontend.client;

import net.robyf.ms.user.api.AuthenticateRequest;
import net.robyf.ms.user.api.AuthenticateResponse;
import net.robyf.ms.user.api.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "user", url = "http://localhost:7000/user-service")
public interface UserServiceClient {

    @PostMapping(value = "/v1/users/authenticate")
    AuthenticateResponse authenticate(@RequestBody AuthenticateRequest request);

    @GetMapping(value = "/v1/users/id/{id}")
    User get(@PathVariable("id") UUID id);

}
