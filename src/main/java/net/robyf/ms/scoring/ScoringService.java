package net.robyf.ms.scoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.scoring.api.ScoringRequest;
import net.robyf.ms.scoring.api.ScoringResponse;
import net.robyf.ms.scoring.persistence.Scoring;
import net.robyf.ms.scoring.persistence.ScoringsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ScoringService {

    private static final BigDecimal THRESHOLD = BigDecimal.valueOf(3000);

    @Autowired
    private ScoringsRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Scoring> getAllScorings() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public ScoringResponse score(final ScoringRequest request) {
        BigDecimal pod = BigDecimal.valueOf(request.getIncome().compareTo(THRESHOLD) < 0 ? 0.9 : 0.1);

        Scoring scoring = Scoring.builder().probabilityOfDefault(pod).request(toJson(request)).build();
        repository.save(scoring);

        return ScoringResponse.builder().scoringId(scoring.getId()).probabilityOfDefault(pod).build();
    }

    private String toJson(final ScoringRequest request) {
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, request);
        } catch (IOException e) {
            log.error("Error serializing", e);
        }
        return writer.toString();
    }

}
