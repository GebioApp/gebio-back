package io.gebio.gebioback.postgres.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "gebio_user")
public class UserEntity {

  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "email")
  private String email;

  @OneToMany(
    mappedBy = "owner",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<BoardEntity> boards;

  @Column(name = "profile_logo")
  private String profileLogo;

  public UserEntity() {}

  public UserEntity(UUID id, String email) {
    this.id = id;
    this.email = email;
  }

  public UUID getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getProfileLogo() {
    return profileLogo;
  }
}
