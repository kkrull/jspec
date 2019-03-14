package info.javaspec.console;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import info.javaspec.console.ArgumentParser.CommandFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(HierarchicalContextRunner.class)
public class StaticCommandFactoryTest {
  private CommandFactory subject;

  @Before
  public void setup() throws Exception {
    subject = new StaticCommandFactory();
  }

  public class helpCommand {
    @Test
    public void returnsHelpCommand() throws Exception {
      Command command = subject.helpCommand();
      assertThat(command, instanceOf(HelpCommand.class));
    }
  }

  public class runSpecsCommand {
    @Test
    public void returnsRunSpecsCommandWithTheGivenClasses() throws Exception {
      Command command = subject.runSpecsCommand(Collections.emptyList());
      assertThat(command, instanceOf(RunSpecsCommand.class));
    }
  }
}