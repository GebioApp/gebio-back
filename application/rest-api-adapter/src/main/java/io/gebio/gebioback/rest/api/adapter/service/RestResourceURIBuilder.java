package io.gebio.gebioback.rest.api.adapter.service;

import java.net.URI;
import java.util.UUID;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public abstract class RestResourceURIBuilder {

  private RestResourceURIBuilder() {}

  public static URI getCreatedResourceURI(UUID resourceId) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(resourceId)
      .toUri();
  }
}
