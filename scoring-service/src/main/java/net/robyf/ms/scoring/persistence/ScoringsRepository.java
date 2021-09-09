package net.robyf.ms.scoring.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ScoringsRepository extends CrudRepository<Scoring, UUID> {
}
