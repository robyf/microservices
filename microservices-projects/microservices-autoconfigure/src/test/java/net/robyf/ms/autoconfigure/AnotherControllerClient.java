package net.robyf.ms.autoconfigure;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "another")
public interface AnotherControllerClient {

    @GetMapping(AnotherController.BASE_PATH + AnotherController.CURRENT_USER_ENDPOINT)
    String currentUser();

}
