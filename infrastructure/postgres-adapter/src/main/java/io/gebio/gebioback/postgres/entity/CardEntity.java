package io.gebio.gebioback.postgres.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "card")
public class CardEntity {

  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "color", nullable = false)
  private String color;

  @Column(name = "pos_x", nullable = false)
  private int posX;

  @Column(name = "pos_y", nullable = false)
  private int posY;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private UserEntity owner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private BoardEntity board;

  public CardEntity(
    UUID id,
    String content,
    String color,
    int posX,
    int posY,
    UserEntity owner
  ) {
    this.id = id;
    this.content = content;
    this.color = color;
    this.posX = posX;
    this.posY = posY;
    this.owner = owner;
  }
}
