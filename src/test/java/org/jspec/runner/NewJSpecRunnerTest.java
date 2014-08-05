package org.jspec.runner;

import static org.jspec.util.Assertions.assertListEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jspec.proto.JSpecExamples;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

import com.google.common.collect.ImmutableList;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class NewJSpecRunnerTest {
  public class constructor {
    @Test
    public void givenAConfigurationWithoutErrors_raisesNoError() {
      runnerFor(configFinding());
    }

    @Test
    public void givenAConfigurationWith1OrMoreErrors_raisesInitializationErrorWithThoseErrors() {
      TestConfiguration config = configFinding(new IllegalArgumentException(), new AssertionError());
      assertInitializationError(config, ImmutableList.of(IllegalArgumentException.class, AssertionError.class));
    }
    
    @Test
    public void givenAContextClassWithNoErrors_raisesNoError() {
      runnerFor(JSpecExamples.One.class);
    }
    
    private TestConfiguration configFinding(Throwable... errors) {
      return new TestConfiguration() {
        @Override
        public List<Throwable> findInitializationErrors() { return Arrays.asList(errors); }

        @Override
        public boolean hasInitializationErrors() { return errors.length > 0; }
      };
    }

    private void assertInitializationError(TestConfiguration config, List<Class<? extends Throwable>> expectedCauses) {
      try {
        new NewJSpecRunner(config);
      } catch (InitializationError ex) {
        assertListEquals(expectedCauses, flattenCauses(ex).map(Throwable::getClass).collect(Collectors.toList()));
        return;
      }
      fail(String.format("Expected causes of initialization error to be <%s>, but nothing was thrown", expectedCauses));
    }
  }
  
  public class run {
    public class givenAContextClass {
      @Test @Ignore
      public void createsAClassTestConfigurationForTheGivenClass() {
        fail("pending");
      }
    }
  }
  
  private static NewJSpecRunner runnerFor(Class<?> contextClass) {
    try {
      return new NewJSpecRunner(contextClass);
    } catch (InitializationError e) {
      return failForInitializationError(e);
    }
  }

  private static NewJSpecRunner runnerFor(TestConfiguration config) {
    try {
      return new NewJSpecRunner(config);
    } catch (InitializationError e) {
      return failForInitializationError(e);
    }
  }

  private static NewJSpecRunner failForInitializationError(InitializationError e) {
    System.out.println("\nInitialization error(s)");
    flattenCauses(e).forEach(x -> {
      System.out.printf("[%s]\n", x.getClass());
      x.printStackTrace(System.out);
    });
    fail("Failed to create JSpecRunner");
    return null;
  }

  private static Stream<Throwable> flattenCauses(InitializationError root) {
    List<Throwable> causes = new LinkedList<Throwable>();
    Stack<InitializationError> nodesWithChildren = new Stack<InitializationError>();
    nodesWithChildren.push(root);
    while (!nodesWithChildren.isEmpty()) {
      InitializationError parent = nodesWithChildren.pop();
      for(Throwable child : parent.getCauses()) {
        if(child instanceof InitializationError) {
          nodesWithChildren.push((InitializationError) child);
        } else {
          causes.add(child);
        }
      }
    }
    return causes.stream();
  }
}