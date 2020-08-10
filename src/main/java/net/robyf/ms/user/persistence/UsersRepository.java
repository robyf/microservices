package net.robyf.ms.user.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends CrudRepository<PersistenceUser, UUID> {

    Optional<PersistenceUser> findFirstByEmail(String email);

}
