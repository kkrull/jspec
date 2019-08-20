package info.javaspec.console.help;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import info.javaspec.console.Command;
import info.javaspec.console.CommandFactory;
import info.javaspec.console.Reporter;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class HelpArgumentsTest {
  public class parseCommand {
    private HelpArguments subject;
    private CommandFactory commandFactory;

    @Before
    public void setup() throws Exception {
      commandFactory = Mockito.mock(CommandFactory.class);
      subject = new HelpArguments(commandFactory);
    }

    public class givenNoArguments {
      @Test
      public void returnsATopLevelHelpCommand() throws Exception {
        Command toCreate = Mockito.mock(Command.class);
        Mockito.when(commandFactory.helpCommand(Mockito.any()))
          .thenReturn(toCreate);

        assertThat(subject.parseCommand(), Matchers.sameInstance(toCreate));
      }

      @Test @Ignore
      public void usesAPlaintextReporter() throws Exception {
        Reporter reporter = Mockito.mock(Reporter.class);
        subject.parseCommand();
        Mockito.verify(commandFactory).helpCommand(Mockito.same(reporter));
      }
    }
  }
}