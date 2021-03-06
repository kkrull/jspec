package info.javaspec.console;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import info.javaspec.console.Exceptions.CommandAlreadyAdded;
import info.javaspec.console.Exceptions.InvalidArguments;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MultiCommandParser implements Main.ArgumentParser {
  private final JCommander.Builder jCommanderConfig;
  private final JCommanderParameters mainParameters;
  private final Map<String, JCommanderParameters> commandParameters;

  public MultiCommandParser(String executableName, JCommanderParameters mainParameters) {
    this.mainParameters = mainParameters;
    this.commandParameters = new LinkedHashMap<>();
    this.jCommanderConfig = JCommander.newBuilder()
      .programName(executableName)
      .addObject(this.mainParameters);
  }

  public MultiCommandParser addCliCommand(String command, JCommanderParameters parameters) {
    if(this.commandParameters.containsKey(command))
      throw CommandAlreadyAdded.named(command);

    this.commandParameters.put(command, parameters);
    this.jCommanderConfig.addCommand(command, parameters);
    return this;
  }

  @Override
  public Command parseCommand(List<String> arguments) {
    JCommander parser = this.jCommanderConfig.build();
    try {
      parser.parse(arguments.toArray(new String[0]));
    } catch(ParameterException ex) {
      throw Optional.ofNullable(parser.getParsedCommand())
        .map(command -> InvalidArguments.forCommand(command, ex))
        .orElse(InvalidArguments.dueTo(ex));
    }

    String namedCommand = parser.getParsedCommand();
    JCommanderParameters selectedParams = this.commandParameters.getOrDefault(namedCommand, this.mainParameters);
    return selectedParams.toExecutableCommand(parser);
  }

  //Contains a JCommander @Parameter for each possible argument/option available to a CLI command
  public interface JCommanderParameters {
    Command toExecutableCommand(JCommander parser);
  }
}
