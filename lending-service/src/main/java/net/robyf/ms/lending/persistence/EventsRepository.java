package net.robyf.ms.lending.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface EventsRepository extends CrudRepository<PersistenceEvent, UUID> {

    List<PersistenceEvent> findByAccountId(UUID accountId);

}
