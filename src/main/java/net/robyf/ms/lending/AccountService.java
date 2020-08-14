package net.robyf.ms.lending;

import net.robyf.ms.lending.api.Account;
import net.robyf.ms.lending.api.AccountStatus;
import net.robyf.ms.lending.persistence.AccountsRepository;
import net.robyf.ms.lending.persistence.PersistenceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountsRepository accountsRepository;

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

}
