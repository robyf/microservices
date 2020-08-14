package net.robyf.ms.frontend.client;

import net.robyf.ms.lending.api.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(name = "lending", url = "http://localhost:7004/lending-service")
public interface LendingServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/v1/accounts/{userId}")
    Account getByUser(@PathVariable("userId") UUID userId);

}
