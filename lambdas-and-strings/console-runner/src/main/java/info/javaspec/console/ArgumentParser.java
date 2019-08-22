package info.javaspec.console;

import com.beust.jcommander.JCommander;
import info.javaspec.console.help.HelpArguments;
import info.javaspec.lang.lambda.RunArguments;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

final class ArgumentParser implements Main.CommandParser {
  private final CommandFactory commandFactory;
  private final ReporterFactory reporterFactory;

  public ArgumentParser(CommandFactory commandFactory, ReporterFactory reporterFactory) {
    this.commandFactory = commandFactory;
    this.reporterFactory = reporterFactory;
  }

  @Override
  public Command parseCommand(List<String> commandThenArguments) {
    HelpArguments helpArguments = new HelpArguments(this.commandFactory, this.reporterFactory);
    if(commandThenArguments.isEmpty())
      return helpArguments.parseCommand(Collections.emptyList());

    String command = commandThenArguments.get(0);
    List<String> arguments = commandThenArguments.subList(1, commandThenArguments.size());

    Optional<String> helpOnWhat = parseHelpOptionOnAnotherCommand(command, arguments);
    if(helpOnWhat.isPresent())
      return helpArguments.parseCommand(Collections.singletonList(helpOnWhat.get()));

    switch(command) {
      case "help":
        return helpArguments.parseCommand(arguments);

      case "run":
        RunArguments runArguments = new RunArguments(this.commandFactory, this.reporterFactory);
        return runArguments.parseCommand(arguments);

      default:
        throw InvalidCommand.noCommandNamed(command);
    }
  }

  //TODO KDK: Maybe better to have a method that parses the total list into a command and a sub-list of arguments?
  private Optional<String> parseHelpOptionOnAnotherCommand(String command, List<String> arguments) {
    return arguments.stream()
        .filter("--help"::equals)
        .map(unused -> command)
        .findFirst();
  }

  static final class InvalidCommand extends RuntimeException {
    public static InvalidCommand noCommandNamed(String command) {
      String message = String.format("Invalid command: %s", command);
      return new InvalidCommand(message);
    }

    private InvalidCommand(String message) {
      super(message);
    }
  }
}
