package io.gebio.gebioback.domain.service;

import io.gebio.gebioback.domain.model.PostIt;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PostItService {

  private final Map<UUID, PostIt> postItCache = new ConcurrentHashMap<>();

  public PostIt addPostIt(PostIt postIt) {
    postItCache.put(postIt.id(), postIt);
    System.out.println(postItCache.entrySet());
    return postIt;
  }

  @Scheduled(fixedRate = 5000) // Sauvegarde en base toutes les 5 secondes
  public void savePostItsToDatabase() {
    if (!postItCache.isEmpty()) {
      System.out.println(
        "💾 Sauvegarde en base de " + postItCache.size() + " post-its..."
      );
      postItCache.clear();
    }
  }
}
