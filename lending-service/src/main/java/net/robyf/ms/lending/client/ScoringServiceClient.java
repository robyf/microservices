package net.robyf.ms.lending.client;

import net.robyf.ms.scoring.api.ScoringRequest;
import net.robyf.ms.scoring.api.ScoringResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "scoring", url = "http://localhost:7002/scoring-service")
public interface ScoringServiceClient {

    @PostMapping(value = "/v1/scoring/")
    ScoringResponse score(@RequestBody ScoringRequest request);

}
