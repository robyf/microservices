package net.robyf.ms.lending.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditDecisionsRepository extends CrudRepository<PersistenceCreditDecision, UUID> {

    List<PersistenceCreditDecision> findByAccountId(UUID accountId);

    @Query("select cd from PersistenceCreditDecision cd where cd.accountId = ?1 and (cd.status = 'APPROVED' or cd.status = 'PENDING')")
    Optional<PersistenceCreditDecision> findValidByAccountId(UUID accountId);

}
