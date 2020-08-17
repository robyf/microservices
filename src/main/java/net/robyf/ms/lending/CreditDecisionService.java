package net.robyf.ms.lending;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.lending.api.AccountStatus;
import net.robyf.ms.lending.api.CreateCreditDecisionRequest;
import net.robyf.ms.lending.api.CreditDecision;
import net.robyf.ms.lending.api.CreditDecisionStatus;
import net.robyf.ms.lending.client.ScoringServiceClient;
import net.robyf.ms.lending.persistence.AccountsRepository;
import net.robyf.ms.lending.persistence.CreditDecisionsRepository;
import net.robyf.ms.lending.persistence.EventType;
import net.robyf.ms.lending.persistence.EventsRepository;
import net.robyf.ms.lending.persistence.PersistenceAccount;
import net.robyf.ms.lending.persistence.PersistenceCreditDecision;
import net.robyf.ms.lending.persistence.PersistenceEvent;
import net.robyf.ms.scoring.api.ScoringRequest;
import net.robyf.ms.scoring.api.ScoringResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CreditDecisionService {

    @Autowired
    private CreditDecisionsRepository repository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private ScoringServiceClient scoringService;

    public List<CreditDecision> getByAccountId(final UUID accountId) {
        return repository.findByAccountId(accountId).stream().map(cd -> cd.asCreditDecision()).collect(Collectors.toList());
    }

    public CreditDecision getValidByAccountId(final UUID accountId) {
        Optional<PersistenceCreditDecision> pCd = repository.findValidByAccountId(accountId);
        if (pCd.isEmpty()) {
            throw Problem.valueOf(Status.NOT_FOUND, "No valid credit decision found for account " + accountId);
        }
        return pCd.get().asCreditDecision();
    }

    public CreditDecision create(final UUID accountId, final CreateCreditDecisionRequest request) {
        checkActiveAccount(accountId);
        repository.findValidByAccountId(accountId).ifPresent(cd -> {
            throw Problem.valueOf(Status.PRECONDITION_FAILED, "A valid credit decision is already present");
        });

        ScoringRequest scoringRequest = ScoringRequest.builder().income(request.getIncome()).build();
        ScoringResponse scoringResponse = scoringService.score(scoringRequest);

        log.info("Received scoring response: {}", scoringResponse);
        PersistenceCreditDecision pCd = PersistenceCreditDecision.builder()
                .accountId(accountId)
                .scoringId(scoringResponse.getScoringId())
                .status(CreditDecisionStatus.PENDING)
                .build();
        if (scoringResponse.getProbabilityOfDefault().compareTo(BigDecimal.valueOf(0.15)) > 0) {
            pCd.setPositive(false);
            pCd.setAmount(BigDecimal.ZERO);
        } else {
            pCd.setPositive(true);
            pCd.setAmount(BigDecimal.valueOf(5000));
        }
        repository.save(pCd);

        return pCd.asCreditDecision();
    }

    public CreditDecision decline(final UUID accountId, final UUID creditDecisionId) {
        checkActiveAccount(accountId);

        PersistenceCreditDecision pCd = checkCorrectAccount(repository.findById(creditDecisionId), accountId);
        if (pCd.getStatus() != CreditDecisionStatus.PENDING) {
            throw Problem.valueOf(Status.PRECONDITION_FAILED, "Credit decision " + creditDecisionId + " not in PENDING state");
        }

        pCd.setStatus(CreditDecisionStatus.DECLINED);
        repository.save(pCd);

        return pCd.asCreditDecision();
    }

    @Transactional
    public CreditDecision accept(final UUID accountId, final UUID creditDecisionId) {
        PersistenceAccount pAccount = checkActiveAccount(accountId);
        if (pAccount.getStatus() != AccountStatus.NEW) {
            throw Problem.valueOf(Status.PRECONDITION_FAILED, "Account not in NEW state");
        }

        PersistenceCreditDecision pCd = checkCorrectAccount(repository.findById(creditDecisionId), accountId);
        if (pCd.getStatus() != CreditDecisionStatus.PENDING) {
            throw Problem.valueOf(Status.PRECONDITION_FAILED, "Credit decision " + creditDecisionId + " not in PENDING state");
        }

        pCd.setStatus(CreditDecisionStatus.APPROVED);
        repository.save(pCd);

        pAccount.setBalance(pCd.getAmount());
        pAccount.setStatus(AccountStatus.ACTIVE);
        accountsRepository.save(pAccount);

        PersistenceEvent pEvent = PersistenceEvent.builder()
                .accountId(accountId)
                .amount(pCd.getAmount())
                .type(EventType.CREDIT_DECISION_ACCEPTED)
                .time(LocalDateTime.now())
                .build();
        eventsRepository.save(pEvent);

        return pCd.asCreditDecision();
    }

    private PersistenceAccount checkActiveAccount(final UUID accountId) {
        Optional<PersistenceAccount> pAccount = accountsRepository.findById(accountId);
        if (pAccount.isEmpty()) {
            throw Problem.valueOf(Status.PRECONDITION_FAILED, "Account with id " + accountId + " not found");
        }
        if (pAccount.get().getStatus() != AccountStatus.ACTIVE && pAccount.get().getStatus() != AccountStatus.NEW) {
            throw Problem.valueOf(Status.PRECONDITION_FAILED, "Account with id " + accountId + " not in ACTIVE or NEW status");
        }
        return pAccount.get();
    }

    private PersistenceCreditDecision checkCorrectAccount(final Optional<PersistenceCreditDecision> opCd, final UUID accountId) {
        if (opCd.isEmpty()) {
            throw Problem.valueOf(Status.NOT_FOUND, "Credit decision not found");
        }
        PersistenceCreditDecision pCd = opCd.get();
        if (!pCd.getAccountId().equals(accountId)) {
            throw Problem.valueOf(Status.NOT_FOUND, "Credit decision not found");
        }
        return pCd;
    }

}
