package io.gebio.gebioback.postgres.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "gebio_user")
public class UserEntity {

  @Id
  @Column(name = "user_id")
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

  @OneToMany(
    mappedBy = "owner",
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<CardEntity> cards;

  @Column(name = "profile_logo")
  private String profileLogo;

  @Column(name = "username")
  private String username;

  @Column(name = "role")
  private String role;

  @CreationTimestamp
  @Column(name = "creation_date")
  private Instant creationDate;

  @UpdateTimestamp
  @Column(name = "modification_date")
  private Instant modificationDate;

  public UserEntity() {}

  public UserEntity(UUID id, String email, String profileLogo) {
    this.id = id;
    this.email = email;
    this.profileLogo = profileLogo;
  }

  public UserEntity(
    UUID id,
    String email,
    String profileLogo,
    String username,
    String role
  ) {
    this.id = id;
    this.email = email;
    this.profileLogo = profileLogo;
    this.username = username;
    this.role = role;
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

  public String getUsername() {
    return username;
  }

  public String getRole() {
    return role;
  }
}
