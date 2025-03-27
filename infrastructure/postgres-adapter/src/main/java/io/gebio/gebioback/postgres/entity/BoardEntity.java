package io.gebio.gebioback.postgres.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "board")
@NamedEntityGraph(
  name = "Board.withCardsAndOwner",
  attributeNodes = {
    @NamedAttributeNode("cards"), @NamedAttributeNode("owner"),
  }
)
public class BoardEntity {

  @Id
  @Column(name = "board_id")
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private UserEntity owner;

  @Column(name = "name")
  private String name;

  @Column(name = "template_id")
  private UUID templateId;

  @OneToMany(
    mappedBy = "board",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<CardEntity> cards;

  @ManyToMany
  @JoinTable(
    name = "gebio_user_board",
    joinColumns = @JoinColumn(name = "board_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<UserEntity> members;

  @CreationTimestamp
  @Column(name = "creation_date")
  private Instant creationDate;

  @UpdateTimestamp
  @Column(name = "modification_date")
  private Instant modificationDate;

  public BoardEntity() {}

  public BoardEntity(UUID id, String name, UUID templateId, UserEntity owner) {
    this.id = id;
    this.name = name;
    this.templateId = templateId;
    this.owner = owner;
    this.cards = Collections.emptyList();
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public UUID getTemplateId() {
    return templateId;
  }

  public UserEntity getOwner() {
    return owner;
  }

  public List<CardEntity> getCards() {
    return cards;
  }

  public List<UserEntity> getMembers() {
    return members;
  }

  public void setMembers(List<UserEntity> membersEntities) {
    members = membersEntities;
  }

  public void setCards(List<CardEntity> cardEntities) {
    this.cards = cardEntities;
  }

  public void addCard(CardEntity cardEntity) {
    cards.add(cardEntity);
  }

  public void updateCard(CardEntity cardEntity) {
    List<CardEntity> updatedCards = cards
      .stream()
      .map(card -> card.getId() == cardEntity.getId() ? cardEntity : card)
      .toList();
    this.setCards(updatedCards);
  }
}
