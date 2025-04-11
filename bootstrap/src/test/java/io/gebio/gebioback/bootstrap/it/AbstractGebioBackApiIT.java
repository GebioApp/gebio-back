package io.gebio.gebioback.bootstrap.it;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import io.gebio.gebioback.bootstrap.it.configuration.TestJwtDecoderConfiguration;
import io.gebio.gebioback.domain.model.UserRole;
import io.gebio.gebioback.postgres.entity.UserEntity;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.socket.messaging.WebSocketStompClient;
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

  protected WebSocketStompClient stompClient;
  protected StompSession stompSession;

  private static final String GEBIO_EMAIL = "email";
  private static final String GEBIO_LOGO = "logo";

  protected static final String AUTHENTICATED_USER_EMAIL =
    "integration-test.user@gmail.com";
  protected static final String AUTHENTICATED_USER_LOGO = "my-logo-url";
  protected static final String AUTHENTICATED_USER_USERNAME =
    "integration-test";

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
      AUTHENTICATED_USER_LOGO,
      AUTHENTICATED_USER_USERNAME,
      UserRole.USER.name()
    );
    return userRepository.save(userEntity);
  }

  protected UserEntity createAndSaveGuestUser() {
    UUID id = UUID.fromString("802dfe54-cc90-4be6-9bd2-461fcea6f214");
    UserEntity userEntity = new UserEntity(
      id,
      "imaguest@gmail.com",
      "https://i.pravatar.cc/150",
      "iamaguest",
      UserRole.GUEST.name()
    );
    return userRepository.save(userEntity);
  }

  protected <T> T waitForMessage(
    Class<T> responseType,
    String topic,
    Runnable action
  ) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    final T[] responseHolder = (T[]) Array.newInstance(responseType, 1);

    stompSession.subscribe(
      topic,
      new StompFrameHandler() {
        @Override
        public Type getPayloadType(StompHeaders headers) {
          return responseType;
        }

        @Override
        public void handleFrame(@NonNull StompHeaders headers, Object payload) {
          responseHolder[0] = responseType.cast(payload);
          latch.countDown();
        }
      }
    );

    action.run();
    assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
    return responseHolder[0];
  }

  protected static final String GET_CURRENT_USER_API_URL = "/api/v1/me";
  protected static final String CREATE_GUEST_API_URL = "/api/v1/public/guest";
  protected static final String FIND_USER_BOARDS_API_URL =
    "/api/v1/user/%s/board";
  protected static final String CREATE_BOARD_API_URL = "/api/v1/board";
  protected static final String FIND_BOARD_API_URL = "/api/v1/public/board/%s";
  protected static final String ADD_USER_TO_BOARD_API_URL =
    "/api/v1/public/board/%s/add-user";

  protected static final String TOPIC_BOARD_API_URL = "/topic/board/%s";
  protected static final String ADD_CARD_API_URL = "/app/board/add-card";
  protected static final String UPDATED_CARD_API_URL = "/app/board/update-card";
  protected static final String DELETE_CARD_API_URL = "/app/board/delete-card";

  ResultActions doPostWithToken(String endpoint, @Language("json") String body)
    throws Exception {
    return mockMvc.perform(
      post(endpoint)
        .with(jwtToken())
        .content(body)
        .contentType(MediaType.APPLICATION_JSON)
    );
  }

  ResultActions doPostWithoutToken(
    String endpoint,
    @Language("json") String body
  ) throws Exception {
    return mockMvc.perform(
      post(endpoint).content(body).contentType(MediaType.APPLICATION_JSON)
    );
  }

  ResultActions doPostWithoutToken(String endpoint) throws Exception {
    return mockMvc.perform(
      post(endpoint).contentType(MediaType.APPLICATION_JSON)
    );
  }

  ResultActions doGetWithToken(String endpoint) throws Exception {
    return mockMvc.perform(
      get(endpoint).with(jwtToken()).contentType(MediaType.APPLICATION_JSON)
    );
  }

  ResultActions doGetWithoutToken(String endpoint) throws Exception {
    return mockMvc.perform(
      get(endpoint).contentType(MediaType.APPLICATION_JSON)
    );
  }

  public static org.hamcrest.Matcher<String> isDatetimeWithUTCFormat() {
    return matchesPattern(
      "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?([+-]\\d{2}:\\d{2}|Z)$"
    );
  }
}
