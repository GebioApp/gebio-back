package io.gebio.gebioback.postgres.repository;

import io.gebio.gebioback.postgres.entity.BoardEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, UUID> {
  @EntityGraph(attributePaths = { "owner", "cards" })
  Optional<BoardEntity> findById(UUID boardId);
}
