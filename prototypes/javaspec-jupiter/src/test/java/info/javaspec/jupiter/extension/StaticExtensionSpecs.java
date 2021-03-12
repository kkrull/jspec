package info.javaspec.jupiter.extension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Look at me, Jupiter")
public class StaticExtensionSpecs {
  @RegisterExtension
  static NaiveTestFactory testFactory = new NaiveTestFactory(StaticExtensionSpecs.class);

  @Test
  void junitTest() {
    assertEquals(2, 1+1);
  }
}
