package io.gebio.gebioback.postgres.mapper;

import io.gebio.gebiback.domain.model.User;
import io.gebio.gebioback.postgres.entity.UserEntity;

public interface UserMapper {
  static User entityToDomain(UserEntity userEntity) {
    return new User(userEntity.getId(), userEntity.getEmail());
  }
}
