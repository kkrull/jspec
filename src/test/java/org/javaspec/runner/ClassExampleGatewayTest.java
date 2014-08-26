package org.javaspec.runner;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.javaspec.proto.ContextClasses;
import org.javaspec.runner.ClassExampleGateway.UnknownStepExecutionSequenceException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class ClassExampleGatewayTest {
  public class findInitializationErrors {
    public class givenAnyTreeOfInnerClasses {
      @Test
      public void andAClassWith2OrMoreEstablishFields_containsUnknownStepExecutionSequenceException() {
        shouldFindInitializationError(ContextClasses.TwoEstablish.class, UnknownStepExecutionSequenceException.class,
          "Impossible to determine running order of multiple Establish functions in test context org.javaspec.proto.ContextClasses$TwoEstablish");
      }
      
      @Test
      public void andAClassWith2OrMoreBecauseFields_containsUnknownStepExecutionSequenceException() {
        shouldFindInitializationError(ContextClasses.TwoBecause.class, UnknownStepExecutionSequenceException.class,
          "Impossible to determine running order of multiple Because functions in test context org.javaspec.proto.ContextClasses$TwoBecause");
      }
      
      @Test
      public void andAClassWith2OrMoreCleanupFields_containsUnknownStepExecutionSequenceException() {
        shouldFindInitializationError(ContextClasses.TwoCleanup.class, UnknownStepExecutionSequenceException.class,
          "Impossible to determine running order of multiple Cleanup functions in test context org.javaspec.proto.ContextClasses$TwoCleanup");
      }
      
      class andAtLeast1ItFieldSomewhere_andNoClassesWith2OrMoreOfTheSameFixture {
        @Test
        public void returnsEmptyList() {
          ExampleGateway subject = new ClassExampleGateway(ContextClasses.NestedThreeDeep.class);
          assertThat(subject.findInitializationErrors(), equalTo(Collections.emptyList()));
        }
      }
      
      private void shouldFindInitializationError(Class<?> contextType, Class<?> errorType, String errorMsg) {
        ExampleGateway subject = new ClassExampleGateway(contextType);
        assertThat(subject.findInitializationErrors().stream().map(x -> x.getClass()).collect(toList()),
          contains(equalTo(errorType)));
        assertThat(subject.findInitializationErrors().stream().map(Throwable::getMessage).collect(toList()),
          contains(equalTo(errorMsg)));
      }
    }
  }
  
  public class getExamples {
    public class givenAClassWithNoItFieldsAtAnyLevel {
      @Test
      public void returnsNoExamples() {
        assertThat(extractNames(readExamples(ContextClasses.Empty.class)), empty());
      }
    }
    
    public class givenAClassWith1OrMoreItFieldsAtAnyLevel {
      @Test @Ignore("wip")
      public void returnsAnExampleForEachItField() {
        assertThat(extractNames(readExamples(ContextClasses.NestedThreeDeep.class)), contains("asserts"));
      }
    }
    
    private List<String> extractNames(Stream<NewExample> examples) {
      return examples.map(NewExample::describeBehavior).collect(toList());
    }
    
    private Stream<NewExample> readExamples(Class<?> context) {
      ExampleGateway subject = new ClassExampleGateway(context);
      return subject.getExamples();
    }
  }
  
  public class getExampleNames {
//    public class givenAClassWithoutAnyExamples {
//      @Test
//      public void returnsAnEmptyList() {
//        ExampleGateway subject = new ClassExampleGateway(ContextClasses.Empty.class);
//        assertThat(subject.getExampleNames(context), equalTo(false));
//      }
//    }
//    
//    public class givenAClassWith1OrMoreExamples {
//      @Test
//      public void returnsTrue() {
//        ExampleGateway subject = new ClassExampleGateway(ContextClasses.OneIt.class);
//        assertThat(subject.hasExamples(), equalTo(true));
//      }
//    }
  }
  
  public class getRootContext {
    public class givenAClassContainingNoItFieldsInAnyPartOfTheTree {
      @Test
      public void returnsAnEmptyContext() {
        Context rootContext = doGetRootContext(ContextClasses.Empty.class);
        assertThat(rootContext.getSubContexts(), empty());
      }
    }
    
    public class givenAClassContainingStaticNestedClasses {
      @Test
      public void omitsThatSubtreeFromTheReturnedContext() {
        Context rootContext = doGetRootContext(ContextClasses.NestedWithStaticHelperClass.class);
        assert2By1Context(rootContext, "NestedWithStaticHelperClass", "context");
      }
    }
    
    public class givenAClassContaining0OrMoreInnerClasses {
      @Test
      public void returnsAContextNodeForEachInnerClassSubtreeContaining1OrMoreItFields() {
        Context rootContext = doGetRootContext(ContextClasses.Nested3By2.class);
        assertThat(rootContext.name, equalTo("Nested3By2"));
        assertThat(rootContext.getSubContexts(), hasSize(2));
        
        assert2By1Context(rootContext.getSubContexts().get(0), "level2a", "level3a");
        assert2By1Context(rootContext.getSubContexts().get(1), "level2b", "level3b");
      }
      
      @Test
      public void skipsInnerClassSubtreesThatContainNoItFields() {
        Context rootContext = doGetRootContext(ContextClasses.NestedWithInnerHelperClass.class);
        assert2By1Context(rootContext, "NestedWithInnerHelperClass", "context");
      }
    }
    
    private Context doGetRootContext(Class<?> contextClass) {
      ExampleGateway subject = new ClassExampleGateway(contextClass);
      return subject.getRootContext();
    }
    
    private void assert2By1Context(Context context, String parentName, String childName) {
      assertThat(context.name, equalTo(parentName));
      assertThat(context.getSubContexts().stream().map(x -> x.name).collect(toList()), contains(childName));
    }
  }
  
  public class getRootContextName {
    @Test
    public void returnsTheNameOfTheGivenClass() {
      ExampleGateway subject = new ClassExampleGateway(ContextClasses.OneIt.class);
      assertThat(subject.getRootContextName(), equalTo("OneIt"));
    }
  }
  
  public class hasExamples {
    public class givenAClassWithoutAnyExamples {
      @Test
      public void returnsFalse() {
        ExampleGateway subject = new ClassExampleGateway(ContextClasses.Empty.class);
        assertThat(subject.hasExamples(), equalTo(false));
      }
    }
    
    public class givenAClassWith1OrMoreExamples {
      @Test
      public void returnsTrue() {
        ExampleGateway subject = new ClassExampleGateway(ContextClasses.OneIt.class);
        assertThat(subject.hasExamples(), equalTo(true));
      }
    }
  }
}