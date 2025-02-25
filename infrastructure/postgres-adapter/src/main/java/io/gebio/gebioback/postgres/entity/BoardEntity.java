package io.gebio.gebioback.postgres.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "board")
public class BoardEntity {

  @Id
  @Column(name = "id")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private UserEntity owner;

  @Column(name = "name")
  private String name;

  @Column(name = "template_id")
  private UUID templateId;

  public BoardEntity() {}

  public BoardEntity(UUID id, String name, UUID templateId, UserEntity owner) {
    this.id = id;
    this.name = name;
    this.templateId = templateId;
    this.owner = owner;
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
}
