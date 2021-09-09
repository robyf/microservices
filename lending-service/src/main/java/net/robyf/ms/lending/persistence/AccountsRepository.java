package net.robyf.ms.lending.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountsRepository extends CrudRepository<PersistenceAccount, UUID> {

    Optional<PersistenceAccount> findFirstByUserId(UUID userId);

}
