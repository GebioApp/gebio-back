package io.gebio.gebioback.domain.model;

import java.util.UUID;
import org.springframework.lang.Nullable;

public record User(UUID id, String email, @Nullable String profileLogo) {}
