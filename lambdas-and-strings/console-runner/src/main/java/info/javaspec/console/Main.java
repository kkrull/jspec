package info.javaspec.console;

import info.javaspec.console.help.HelpParameters;
import info.javaspec.lang.lambda.RunParameters;

import java.util.Arrays;
import java.util.List;

public final class Main {
  private final Reporter reporter;
  private final ExitHandler system;

  public static void main(String... args) {
    //Responsible just for wiring things up with concrete classes.  Delegates all behavior, to make it testable.
    ReporterFactory reporterFactory = new StaticReporterFactory(System.out);
    main(
      cliArgumentParser(new StaticCommandFactory(), reporterFactory),
      reporterFactory,
      System::exit,
      args
    );
  }

  static void main(ArgumentParser cliParser, ReporterFactory reporterFactory, ExitHandler system, String... args) {
    //Responsible for enabling testing with doubles by doing all logic (even if messy) via interfaces
    Command runnableCommand = null;
    try {
      runnableCommand = cliParser.parseCommand(Arrays.asList(args));
    } catch (Exception e) {
      System.err.println("run: The following options are required: [--reporter], [--spec-classpath]");
      system.exit(1);
      return; //Runs during a test, but not during production (suggesting that Result be passed upwards)
    }

    Main cli = new Main(reporterFactory.plainTextReporter(), system);
    cli.runCommand(runnableCommand);
  }

  static ArgumentParser cliArgumentParser(CommandFactory commandFactory, ReporterFactory reporterFactory) {
    MainParameters mainParameters = new MainParameters(commandFactory, reporterFactory);
    return new MultiCommandParser("javaspec", mainParameters)
      .addCliCommand("help", new HelpParameters(commandFactory, reporterFactory))
      .addCliCommand("run", new RunParameters(commandFactory, reporterFactory));
  }

  private Main(Reporter reporter, ExitHandler system) {
    this.reporter = reporter;
    this.system = system;
  }

  private void runCommand(Command command) {
    Result result = command.run();
    result.reportTo(this.reporter);
    this.system.exit(result.exitCode);
  }

  //Parses an entire command line into an executable command
  @FunctionalInterface
  interface ArgumentParser {
    Command parseCommand(List<String> arguments);
  }

  @FunctionalInterface
  interface ExitHandler {
    void exit(int statusCode);
  }
}
