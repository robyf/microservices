package net.robyf.ms.frontend.client;

import com.netflix.hystrix.HystrixCommand;
import net.robyf.ms.user.api.AuthenticateRequest;
import net.robyf.ms.user.api.AuthenticateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user", url = "http://localhost:7000/user-service")
public interface UserServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/v1/users/authenticate")
    AuthenticateResponse authenticate(@RequestBody AuthenticateRequest request);

}
