package io.gebio.gebioback.rest.api.adapter.controller;

import io.gebio.gebioback.domain.service.PostItService;
import io.gebio.gebioback.rest.api.adapter.mapper.PostItContractMapper;
import io.gebio.gebioback.rest.api.adapter.model.AddPostItContract;
import io.gebio.gebioback.rest.api.adapter.model.AddPostItResponseContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostItWebSocketController {

  private static final Logger logger = LoggerFactory.getLogger(
    PostItWebSocketController.class
  );

  private final PostItService postItService;

  public PostItWebSocketController(PostItService postItService) {
    this.postItService = postItService;
  }

  @MessageMapping("/postit/add")
  @SendTo("/topic/board")
  public AddPostItResponseContract addPostIt(
    @Payload AddPostItContract addPostItContract
  ) {
    logger.info("📝 Nouveau post-it reçu: {}", addPostItContract);
    return PostItContractMapper.domainToResponseContract(
      postItService.addPostIt(
        PostItContractMapper.contractToDomain(addPostItContract)
      )
    );
  }
}
