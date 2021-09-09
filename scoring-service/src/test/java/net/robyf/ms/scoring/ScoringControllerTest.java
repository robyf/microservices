package net.robyf.ms.scoring;

import net.robyf.ms.scoring.api.ScoringRequest;
import net.robyf.ms.scoring.api.ScoringResponse;
import net.robyf.ms.scoring.persistence.Scoring;
import net.robyf.ms.scoring.persistence.ScoringsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScoringControllerTest {

    @Autowired
    private ScoringsRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @After
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void testScoringLowIncome() {
        ScoringRequest request = ScoringRequest.builder().income(BigDecimal.valueOf(100)).build();
        String uri = UriComponentsBuilder.fromPath(ScoringController.BASE_PATH + ScoringController.SCORE_ENDPOINT).build().toUriString();
        ResponseEntity<ScoringResponse> get = restTemplate.postForEntity(uri, request, ScoringResponse.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        ScoringResponse response = get.getBody();
        assertThat(response.getScoringId()).isNotNull();
        assertThat(response.getProbabilityOfDefault()).isEqualTo(BigDecimal.valueOf(0.9));

        Optional<Scoring> oScoring = repository.findById(response.getScoringId());
        assertThat(oScoring).isNotEmpty();
        Scoring scoring = oScoring.get();
        assertThat(scoring.getProbabilityOfDefault()).isEqualByComparingTo(response.getProbabilityOfDefault());
    }

    @Test
    public void testScoringHighIncome() {
        ScoringRequest request = ScoringRequest.builder().income(BigDecimal.valueOf(10000)).build();
        String uri = UriComponentsBuilder.fromPath(ScoringController.BASE_PATH + ScoringController.SCORE_ENDPOINT).build().toUriString();
        ResponseEntity<ScoringResponse> get = restTemplate.postForEntity(uri, request, ScoringResponse.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        ScoringResponse response = get.getBody();
        assertThat(response.getScoringId()).isNotNull();
        assertThat(response.getProbabilityOfDefault()).isEqualTo(BigDecimal.valueOf(0.1));

        Optional<Scoring> oScoring = repository.findById(response.getScoringId());
        assertThat(oScoring).isNotEmpty();
        Scoring scoring = oScoring.get();
        assertThat(scoring.getProbabilityOfDefault()).isEqualByComparingTo(response.getProbabilityOfDefault());
    }

    @Test
    public void testListEmptyDb() {
        String uri = UriComponentsBuilder.fromPath(ScoringController.BASE_PATH + ScoringController.LIST_ENDPOINT).build().toUriString();
        ResponseEntity<Scoring[]> get = restTemplate.getForEntity(uri, Scoring[].class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        assertThat(get.getBody()).isEmpty();
    }

}
