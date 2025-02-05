package io.gebio.gebioback.rest.api.adapter.controller;

import io.gebio.gebioback.contract.api.HealthApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements HealthApi {

  @Override
  public ResponseEntity<String> check() {
    return ResponseEntity.ok("OK, API is running");
  }
}
