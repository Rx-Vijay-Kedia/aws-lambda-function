package com.example.function;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;

@NotNull
public class ScheduledEventHandler implements Consumer<Message<S3Event>> {

  @Override
  public void accept(Message<S3Event> s3EventMessage) {

  }
}
