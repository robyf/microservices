package net.robyf.ms.lending;

import net.robyf.ms.lending.api.Event;
import net.robyf.ms.lending.persistence.EventsRepository;
import net.robyf.ms.lending.persistence.PersistenceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventsRepository repository;

    public List<Event> getByAccountId(final UUID accountId) {
        return repository.findByAccountId(accountId).stream().map(PersistenceEvent::asEvent).collect(Collectors.toList());
    }

}
