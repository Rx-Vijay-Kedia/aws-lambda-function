package com.example.function;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.example.Application;
import com.example.TestLogger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

abstract class AbstractFunctionInvokerTest {

  void beforeEachTestSetup() {
    System.clearProperty("MAIN_CLASS");
    System.clearProperty("spring.cloud.function.definition");
  }

  void runFunction(@NotNull final String functionName, @NotNull final String eventResource)
      throws IOException {
    System.setProperty("MAIN_CLASS", Application.class.getName());
    System.setProperty("spring.cloud.function.definition", Objects.requireNonNull(functionName));
    final FunctionInvoker invoker = new FunctionInvoker();

    final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(eventResource);
    Assertions.assertNotNull(inputStream);
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    invoker.handleRequest(
        inputStream,
        outputStream,
        new Context() {
          final UUID uuid = UUID.randomUUID();

          @Override
          public String getAwsRequestId() {
            return uuid.toString();
          }

          @Override
          public String getLogGroupName() {
            return "/aws/lambda/" + functionName;
          }

          @Override
          public String getLogStreamName() {

            return LocalDate.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy/mm/dd"))
                + "/[$LATEST]"
                + uuid.toString().replace("-", "");
          }

          @Override
          public String getFunctionName() {
            return functionName;
          }

          @Override
          public String getFunctionVersion() {
            return "$LATEST";
          }

          @Override
          public String getInvokedFunctionArn() {
            return "arn:aws:lambda:us-east-2:123456789012:function:" + functionName;
          }

          @Override
          public CognitoIdentity getIdentity() {
            return null;
          }

          @Override
          public ClientContext getClientContext() {
            return null;
          }

          @Override
          public int getRemainingTimeInMillis() {
            return 30000;
          }

          @Override
          public int getMemoryLimitInMB() {
            return 512;
          }

          @Override
          public LambdaLogger getLogger() {
            return new TestLogger();
          }
        });
  }
}
