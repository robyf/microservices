package net.robyf.ms.lending;

import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.AccountStatus;
import net.robyf.ms.lending.api.MonetaryTransactionRequest;
import net.robyf.ms.lending.api.MonetaryTransactionResponse;
import net.robyf.ms.lending.persistence.AccountsRepository;
import net.robyf.ms.lending.persistence.EventType;
import net.robyf.ms.lending.persistence.EventsRepository;
import net.robyf.ms.lending.persistence.PersistenceAccount;
import net.robyf.ms.lending.persistence.PersistenceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    public Account getByUserId(final UUID userId) {
        Optional<PersistenceAccount> pAccount = accountsRepository.findFirstByUserId(userId);
        if (pAccount.isEmpty()) {
            throw Problem.valueOf(Status.NOT_FOUND, "Lending account not found for user with id: " + userId);
        }
        return pAccount.get().asAccount();
    }

    public Account create(final UUID userId) {
        if (accountsRepository.findFirstByUserId(userId).isPresent()) {
            throw Problem.valueOf(Status.PRECONDITION_FAILED, "Lending account for user with id: " + userId + " already present");
        }
        PersistenceAccount pAccount = PersistenceAccount.builder()
                .userId(userId)
                .status(AccountStatus.NEW)
                .balance(BigDecimal.ZERO)
                .build();
        accountsRepository.save(pAccount);
        return pAccount.asAccount();
    }

    @Transactional
    public MonetaryTransactionResponse deposit(final UUID userId, final UUID accountId, final MonetaryTransactionRequest request) {
        Optional<PersistenceAccount> opAccount = accountsRepository.findById(accountId);
        if (opAccount.isEmpty()) {
            throw Problem.valueOf(Status.NOT_FOUND, "Lending account " + accountId + " not found");
        }
        PersistenceAccount pAccount = opAccount.get();
        if (!pAccount.getUserId().equals(userId)) {
            throw Problem.valueOf(Status.NOT_FOUND, "Lending account " + accountId + " not found");
        }

        pAccount.setBalance(pAccount.getBalance().add(request.getAmount()));
        accountsRepository.save(pAccount);

        PersistenceEvent pEvent = PersistenceEvent.builder()
                .accountId(accountId)
                .amount(request.getAmount())
                .resultingBalance(pAccount.getBalance())
                .type(EventType.DEPOSIT)
                .time(LocalDateTime.now())
                .build();
        eventsRepository.save(pEvent);

        return MonetaryTransactionResponse.builder().resultingBalance(pAccount.getBalance()).build();
    }

    @Transactional
    public MonetaryTransactionResponse withdraw(final UUID userId, final UUID accountId, final MonetaryTransactionRequest request) {
        Optional<PersistenceAccount> opAccount = accountsRepository.findById(accountId);
        if (opAccount.isEmpty()) {
            throw Problem.valueOf(Status.NOT_FOUND, "Lending account " + accountId + " not found");
        }

        PersistenceAccount pAccount = opAccount.get();
        if (!pAccount.getUserId().equals(userId)) {
            throw Problem.valueOf(Status.NOT_FOUND, "Lending account " + accountId + " not found");
        }

        if (pAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw Problem.valueOf(Status.PRECONDITION_FAILED, "Insufficient balance");
        }

        pAccount.setBalance(pAccount.getBalance().subtract(request.getAmount()));
        accountsRepository.save(pAccount);

        PersistenceEvent pEvent = PersistenceEvent.builder()
                .accountId(accountId)
                .amount(request.getAmount())
                .resultingBalance(pAccount.getBalance())
                .type(EventType.WITHDRAW)
                .time(LocalDateTime.now())
                .build();
        eventsRepository.save(pEvent);

        return MonetaryTransactionResponse.builder().resultingBalance(pAccount.getBalance()).build();
    }

}
