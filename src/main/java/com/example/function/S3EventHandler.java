package com.example.function;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component(value = "s3")
public class S3EventHandler implements Consumer<Message<S3Event>> {

  private static final Logger logger = LoggerFactory.getLogger(S3EventHandler.class);

  private final ObjectMapper objectMapper;

  @Autowired
  public S3EventHandler(@NotNull final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void accept(@NotNull final Message<S3Event> message) {

    try {
      final Object context = message.getHeaders().get("aws-context");
      objectMapper.writeValueAsString(context);
    } catch (final JsonProcessingException e) {
      logger.error("Unknown exception occurred", e);
    }
  }
}
