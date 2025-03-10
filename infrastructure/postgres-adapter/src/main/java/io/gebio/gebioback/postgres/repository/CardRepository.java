package io.gebio.gebioback.postgres.repository;

import io.gebio.gebioback.postgres.entity.CardEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, UUID> {}
