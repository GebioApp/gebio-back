package io.gebio.gebioback.bootstrap.it;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import io.gebio.gebioback.bootstrap.it.configuration.TestJwtDecoderConfiguration;
import io.gebio.gebioback.postgres.entity.UserEntity;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("it")
@Import(TestJwtDecoderConfiguration.class)
class AbstractGebioBackApiIT {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected UserRepository userRepository;

  private static final String GEBIO_EMAIL = "email";
  private static final String GEBIO_LOGO = "logo";

  protected static final String AUTHENTICATED_USER_EMAIL =
    "integration-test.user@gmail.com";
  protected static final String AUTHENTICATED_USER_LOGO = "my-logo-url";

  @Container
  static PostgreSQLContainer postgresSQLContainer = new PostgreSQLContainer<>(
    "postgres:17"
  ).withDatabaseName("gebio-postgres-it");

  @BeforeAll
  static void startContainer() {
    postgresSQLContainer.start();
  }

  @DynamicPropertySource
  static void updateProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresSQLContainer::getJdbcUrl);
    registry.add(
      "spring.datasource.password",
      postgresSQLContainer::getPassword
    );
    registry.add(
      "spring.datasource.username",
      postgresSQLContainer::getUsername
    );
  }

  protected static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtToken() {
    return jwt()
      .jwt(
        Jwt.withTokenValue("token")
          .header("alg", "none")
          .claim(GEBIO_EMAIL, AUTHENTICATED_USER_EMAIL)
          .claim(GEBIO_LOGO, AUTHENTICATED_USER_LOGO)
          .build()
      );
  }

  protected UserEntity createAndSaveAuthenticatedUser() {
    UUID id = UUID.fromString("3338266c-26f2-4c85-8157-91f02b680577");
    UserEntity userEntity = new UserEntity(
      id,
      AUTHENTICATED_USER_EMAIL,
      AUTHENTICATED_USER_LOGO
    );
    return userRepository.save(userEntity);
  }

  protected UserEntity createAndSaveGuestUser() {
    UUID id = UUID.fromString("802dfe54-cc90-4be6-9bd2-461fcea6f214");
    UserEntity userEntity = new UserEntity(
      id,
      "imaguest@gmail.com",
      "https://i.pravatar.cc/150"
    );
    return userRepository.save(userEntity);
  }

  protected static final String GET_CURRENT_USER_API_URL = "/api/v1/me";
  protected static final String CREATE_BOARD_API_URL = "/api/v1/board";
  protected static final String FIND_BOARD_API_URL = "/api/v1/board/%s";

  protected static final String TOPIC_BOARD_API_URL = "/topic/board/%s";
  protected static final String ADD_CARD_API_URL = "/app/board/add-card";
  protected static final String UPDATED_CARD_API_URL = "/app/board/update-card";
  protected static final String DELETE_CARD_API_URL = "/app/board/delete-card";
}
