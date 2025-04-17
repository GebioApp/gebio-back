package io.gebio.gebioback.domain.model;

import java.util.UUID;

public record BoardDeleteCommand(UUID boardId, UUID userId) {}
