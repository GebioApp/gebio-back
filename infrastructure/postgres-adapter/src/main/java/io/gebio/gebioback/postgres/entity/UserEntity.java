package io.gebio.gebioback.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "user", schema = "public")
public class UserEntity {

  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "email")
  private String email;

  public UUID getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }
}
