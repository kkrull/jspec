package org.javaspec.runner;

import static org.hamcrest.Matchers.*;
import static org.javaspec.testutil.Assertions.assertThrows;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.javaspec.proto.ContextClasses;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class OldFieldExampleTest {
  public class descriptionMethods {
    public class givenNoFixtureFields {
      private final IOldExample subject = exampleWithIt(ContextClasses.OneIt.class, "only_test");
      
      @Test
      public void fixtureMethodDescriptors_returnBlank() {
        assertThat(subject.describeSetup(), equalTo(""));
        assertThat(subject.describeAction(), equalTo(""));
        assertThat(subject.describeCleanup(), equalTo(""));
      }
    }
    
    public class givenAValueForAField {
      private final IOldExample subject = exampleWithFullFixture();
      
      @Test
      public void returnsTheNameOfTheField() {
        assertThat(subject.describeSetup(), equalTo("arranges"));
        assertThat(subject.describeAction(), equalTo("acts"));
        assertThat(subject.describeBehavior(), equalTo("asserts"));
        assertThat(subject.describeCleanup(), equalTo("cleans"));
      }
    }
  }
  
  public class isSkipped {
    public class givenAnItFieldAndAnyOtherJavaSpecFields {
      public class whenEachJavaSpecFieldHasAnAssignedValue {
        @Test
        public void returnsFalse() {
          shouldBeSkipped(ContextClasses.FullFixture.class, false);
        }
      }
      
      public class when1OrMoreJavaSpecFieldsDoNotHaveAnAssignedValue {
        @Test
        public void returnsTrue() {
          shouldBeSkipped(ContextClasses.PendingEstablish.class, true);
          shouldBeSkipped(ContextClasses.PendingBecause.class, true);
          shouldBeSkipped(ContextClasses.PendingIt.class, true);
          assertThat(
            exampleWith(ContextClasses.PendingCleanup.class, "arranges", "acts", "asserts", "cleans").isSkipped(),
            equalTo(true));
        }
      }
    }
    
    private void shouldBeSkipped(Class<?> contextClass, boolean isSkipped) {
      IOldExample subject = exampleWith(contextClass, "arranges", "acts", "asserts", null);
      assertThat(subject.isSkipped(), equalTo(isSkipped));
    }
  }
  
  public class run {
    public class givenAClassWithoutACallableNoArgConstructor {
      @Test
      public void ThrowsUnsupportedConstructorException() {
        assertThrowsUnsupportedConstructorException(ContextClasses.ConstructorHidden.class, "is_otherwise_valid");
        assertThrowsUnsupportedConstructorException(ContextClasses.ConstructorWithArguments.class, "is_otherwise_valid");
      }
      
      private void assertThrowsUnsupportedConstructorException(Class<?> context, String itFieldName) {
        IOldExample subject = exampleWithIt(context, itFieldName);
        assertThrows(FieldExample.UnsupportedConstructorException.class,
          is(String.format("Unable to find a no-argument constructor for class %s", context.getName())),
          NoSuchMethodException.class, subject::run);
      }
    }
    
    public class givenAFaultyConstructorOrInitializer {
      @Test
      public void throwsTestSetupException() throws Exception {
        assertTestSetupException(ContextClasses.FailingClassInitializer.class, "will_fail", AssertionError.class);
        assertTestSetupException(ContextClasses.FailingConstructor.class, "will_fail", InvocationTargetException.class);
      }
      
      private void assertTestSetupException(Class<?> context, String itFieldName, Class<? extends Throwable> cause) {
        IOldExample subject = exampleWithIt(context, itFieldName);
        assertThrows(FieldExample.TestSetupException.class, 
          is(String.format("Failed to create test context %s", context.getName())),
          cause, subject::run);
      }
    }
    
    public class givenAccessibleFields {
      private final List<String> events = new LinkedList<String>();
      private final IOldExample subject = exampleWithFullFixture();
      
      @Before
      public void spy() throws Exception {
        ContextClasses.FullFixture.setEventListener(events::add);
        subject.run();
      }
      
      @After
      public void releaseSpy() {
        ContextClasses.FullFixture.setEventListener(null);
      }
      
      @Test
      public void runsTheActionFunctionBeforeTheItFunction() throws Exception {
        assertThat(events, contains(
          "ContextClasses.FullFixture::new",
          "ContextClasses.FullFixture::arrange",
          "ContextClasses.FullFixture::act",
          "ContextClasses.FullFixture::assert",
          "ContextClasses.FullFixture::cleans"));
      }
    }
    
    public class whenAFieldCanNotBeAccessed {
      private final IOldExample subject = exampleWithIt(HasWrongType.class, "inaccessibleAsIt");
      
      @Test
      public void throwsTestSetupExceptionCausedByReflectionError() {
        //Intended to catch ReflectiveOperationException, but causing that with a fake SecurityManager was not reliable
        assertThrows(FieldExample.TestSetupException.class, startsWith("Failed to create test context"), ClassCastException.class,
          subject::run);
      }
    }
    
    public class whenATestFunctionThrows {
      @Test
      public void throwsWhateverEstablishThrows() {
        IOldExample subject = exampleWithEstablish(ContextClasses.FailingEstablish.class, "flawed_setup", "will_never_run");
        assertThrows(UnsupportedOperationException.class, equalTo("flawed_setup"), subject::run);
      }
      
      @Test
      public void throwsWhateverBecauseThrows() {
        IOldExample subject = exampleWithBecause(ContextClasses.FailingBecause.class, "flawed_action", "will_never_run");
        assertThrows(UnsupportedOperationException.class, equalTo("flawed_action"), subject::run);
      }
      
      @Test
      public void throwsWhateverItThrows() {
        IOldExample subject = exampleWithIt(ContextClasses.FailingIt.class, "fails");
        assertThrows(AssertionError.class, anything(), subject::run);
      }
      
      @Test
      public void throwsWhateverCleanupThrows() {
        IOldExample subject = exampleWithCleanup(ContextClasses.FailingCleanup.class, "may_run", "flawed_cleanup");
        assertThrows(IllegalStateException.class, equalTo("flawed_cleanup"), subject::run);
      }
    }

    public class givenACleanupField {
      public class whenASetupActionOrAssertionFunctionThrows {
        private final List<String> events = new LinkedList<String>();
        private final IOldExample subject = exampleWith(ContextClasses.FailingEstablishWithCleanup.class,
          "establish", null, "it", "cleanup");
        private Throwable thrown;
        
        @Before
        public void spy() throws Exception {
          ContextClasses.FailingEstablishWithCleanup.setEventListener(events::add);
          try {
            subject.run();
          } catch (Throwable t) {
            this.thrown = t;
          }
        }
        
        @After
        public void releaseSpy() {
          ContextClasses.FailingEstablishWithCleanup.setEventListener(null);
        }
        
        @Test
        public void runsTheCleanupInCaseAnyStatementsExecutedBeforeTheError() {
          assertThat(events, contains(
            "ContextClasses.FailingEstablishWithCleanup::establish",
            "ContextClasses.FailingEstablishWithCleanup::cleanup"));
        }
        
        @Test
        public void stillThrowsTheOriginalException() {
          assertThat(thrown.getMessage(), equalTo("flawed_setup"));
        }
      }
    }
  }
  
  public class whenAccessingFieldsMultipleTimes {
    private final IOldExample subject = exampleWithIt(ContextClasses.UnstableConstructor.class, "asserts");
    
    @Test
    public void onlyConstructsTheClassOnce() throws Exception {
      //General precautions; it could get confusing if a context class manages to change state between isSkipped and run
      subject.isSkipped();
      subject.run();
    }
  }
  
  private static IOldExample exampleWithEstablish(Class<?> context, String establishField, String itField) {
    return exampleWith(context, establishField, null, itField, null);
  }
  
  private static IOldExample exampleWithBecause(Class<?> context, String becauseField, String itField) {
    return exampleWith(context, null, becauseField, itField, null);
  }
  
  private static IOldExample exampleWithIt(Class<?> context, String name) {
    return exampleWith(context, null, null, name, null);
  }
  
  private static IOldExample exampleWithCleanup(Class<?> context, String itField, String cleanupField) {
    return exampleWith(context, null, null, itField, cleanupField);
  }
  
  private static IOldExample exampleWithFullFixture() {
    return exampleWith(ContextClasses.FullFixture.class, "arranges", "acts", "asserts", "cleans");
  }
  
  private static IOldExample exampleWith(Class<?> context, String establish, String because, String it, String cleanup) {
    try {
      return new OldFieldExample(
        establish == null ? null : context.getDeclaredField(establish),
        because == null ? null : context.getDeclaredField(because),
        it == null ? null : context.getDeclaredField(it),
        cleanup == null ? null : context.getDeclaredField(cleanup));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public static class HasWrongType {
    public Object inaccessibleAsIt = new Object();
  }
}