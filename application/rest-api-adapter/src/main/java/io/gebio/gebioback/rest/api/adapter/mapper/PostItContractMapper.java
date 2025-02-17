package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.domain.model.PostIt;
import io.gebio.gebioback.rest.api.adapter.model.AddPostItContract;
import io.gebio.gebioback.rest.api.adapter.model.AddPostItResponseContract;

public interface PostItContractMapper {
  static PostIt contractToDomain(AddPostItContract addPostItContract) {
    return new PostIt(addPostItContract.id(), addPostItContract.message());
  }

  static AddPostItResponseContract domainToResponseContract(PostIt postIt) {
    return new AddPostItResponseContract(postIt.id(), postIt.message());
  }
}
