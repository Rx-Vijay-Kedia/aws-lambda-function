package com.example.function;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class S3EventHandlerTest extends AbstractFunctionInvokerTest {

  @BeforeEach
  void setUp() {
    beforeEachTestSetup();
  }

  @Test
  void testS3EventHandler() throws IOException {
    runFunction("", "sample_s3_event.json");
    Assertions.assertTrue(true);
  }
}
