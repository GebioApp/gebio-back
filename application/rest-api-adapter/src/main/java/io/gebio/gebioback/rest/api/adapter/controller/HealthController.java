package io.gebio.gebioback.rest.api.adapter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("health")
public class HealthController {

  @GetMapping("check")
  public String check() {
    return "OK";
  }
}
