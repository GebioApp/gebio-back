package io.gebio.gebioback.bootstrap.it;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class AbstractGebioBackApiIT {

  @Autowired
  protected MockMvc mockMvc;

  private static final String GEBIO_EMAIL = "test/email";

  protected static final String EMAIL = "integration-test.user@gmail.com";

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
          .claim(GEBIO_EMAIL, EMAIL)
          .build()
      );
  }

  protected static final String GET_CURRENT_USER_API_URL = "/api/v1/me";
}
