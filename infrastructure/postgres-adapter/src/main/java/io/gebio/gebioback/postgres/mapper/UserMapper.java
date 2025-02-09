package io.gebio.gebioback.postgres.mapper;

import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.postgres.entity.UserEntity;

public interface UserMapper {
  static User entityToDomain(UserEntity userEntity) {
    return new User(userEntity.getId(), userEntity.getEmail());
  }

  static UserEntity domainToEntity(User user) {
    return new UserEntity(user.id(), user.email());
  }
}
