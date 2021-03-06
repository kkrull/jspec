package info.javaspec.lang.lambda;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import info.javaspec.RunObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(HierarchicalContextRunner.class)
public class DescriptiveSpecTest {
  private DescriptiveSpec subject;

  public class intendedBehavior {
    @Test
    public void returnsTheGivenDescription() throws Exception {
      subject = new DescriptiveSpec("behaves", anyBehaviorVerification());
      assertThat(subject.intendedBehavior(), equalTo("behaves"));
    }
  }

  public class run {
    private RunObserver observer;

    @Before
    public void setup() throws Exception {
      observer = Mockito.mock(RunObserver.class);
    }

    @Test
    public void reportsTheSpecStarting() throws Exception {
      subject = new DescriptiveSpec(anyIntendedBehavior(), anyBehaviorVerification());
      subject.run(observer);
      Mockito.verify(observer).specStarting(subject);
    }

    @Test
    public void runsTheGivenBehaviorVerification() throws Exception {
      BehaviorVerification verification = Mockito.mock(BehaviorVerification.class);
      subject = new DescriptiveSpec(anyIntendedBehavior(), verification);
      subject.run(observer);
      Mockito.verify(verification).run();
    }

    @Test
    public void reportsAPassingSpecWhenTheVerificationDoesNotThrowAnything() throws Exception {
      subject = new DescriptiveSpec(anyIntendedBehavior(), anyBehaviorVerification());

      subject.run(observer);
      Mockito.verify(observer).specPassed(subject);

      Mockito.verify(observer, Mockito.never()).specFailed(
        Mockito.same(subject),
        Mockito.any(AssertionError.class)
      );

      Mockito.verify(observer, Mockito.never()).specFailed(
        Mockito.same(subject),
        Mockito.any(Exception.class)
      );
    }

    @Test
    public void reportsAFailingSpecWhenTheVerificationThrowsAssertionError() throws Exception {
      AssertionError error = new AssertionError();
      subject = new DescriptiveSpec(anyIntendedBehavior(), () -> { throw error; });

      subject.run(observer);
      Mockito.verify(observer).specFailed(subject, error);
      Mockito.verify(observer, Mockito.never()).specPassed(subject);
    }

    @Test
    public void reportsAFailingSpecWhenTheVerificationThrowsExceptions() throws Exception {
      RuntimeException exception = new RuntimeException();
      subject = new DescriptiveSpec(anyIntendedBehavior(), () -> { throw exception; });

      subject.run(observer);
      Mockito.verify(observer).specFailed(subject, exception);
      Mockito.verify(observer, Mockito.never()).specPassed(subject);
    }
  }

  private BehaviorVerification anyBehaviorVerification() {
    return () -> { };
  }

  private String anyIntendedBehavior() {
    return "";
  }
}
