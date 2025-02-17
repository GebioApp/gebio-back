package io.gebio.gebioback.rest.api.adapter.model;

import java.util.UUID;

public record AddPostItResponseContract(UUID id, String message) {}
