package io.gebio.gebioback.postgres.repository;

import io.gebio.gebioback.postgres.entity.BoardEntity;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CrudRepository<BoardEntity, UUID> {}
