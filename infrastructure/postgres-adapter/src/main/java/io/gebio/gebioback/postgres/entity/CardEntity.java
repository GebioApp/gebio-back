package io.gebio.gebioback.postgres.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "card")
public class CardEntity {

  @Id
  @Column(name = "card_id", nullable = false)
  private UUID id;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "color", nullable = false)
  private String color;

  @Column(name = "pos_x", nullable = false)
  private int posX;

  @Column(name = "pos_y", nullable = false)
  private int posY;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private UserEntity owner;

  @ManyToOne
  @JoinColumn(name = "board_id")
  private BoardEntity board;

  @CreationTimestamp
  @Column(name = "creation_date")
  private Instant creationDate;

  @UpdateTimestamp
  @Column(name = "modification_date")
  private Instant modificationDate;

  public CardEntity() {}

  public CardEntity(
    UUID id,
    String content,
    String color,
    int posX,
    int posY,
    UserEntity owner,
    BoardEntity board
  ) {
    this.id = id;
    this.content = content;
    this.color = color;
    this.posX = posX;
    this.posY = posY;
    this.owner = owner;
    this.board = board;
  }

  public UUID getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public String getColor() {
    return color;
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }

  public UserEntity getOwner() {
    return owner;
  }

  public BoardEntity getBoard() {
    return board;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public void setPosX(int posX) {
    this.posX = posX;
  }

  public void setPosY(int posY) {
    this.posY = posY;
  }

  public void setOwner(UserEntity owner) {
    this.owner = owner;
  }

  public void setBoard(BoardEntity board) {
    this.board = board;
  }
}
