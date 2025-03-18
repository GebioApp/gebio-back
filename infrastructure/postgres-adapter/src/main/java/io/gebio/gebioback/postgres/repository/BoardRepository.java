package io.gebio.gebioback.postgres.repository;

import io.gebio.gebioback.postgres.entity.BoardEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, UUID> {
  @EntityGraph(attributePaths = { "owner", "cards" })
  Optional<BoardEntity> findById(UUID boardId);

  @EntityGraph(
    value = "Board.withCardsAndOwner",
    type = EntityGraph.EntityGraphType.FETCH
  )
  @Query(
    "SELECT board FROM BoardEntity board left join board.members members where members.id = :memberId"
  )
  List<BoardEntity> findByMemberIdsContain(@Param("memberId") UUID memberId);
}
