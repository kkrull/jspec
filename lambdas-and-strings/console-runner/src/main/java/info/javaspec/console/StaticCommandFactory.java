package info.javaspec.console;

import com.beust.jcommander.JCommander;
import info.javaspec.RunObserver;
import info.javaspec.console.help.HelpCommand;
import info.javaspec.console.help.HelpObserver;
import info.javaspec.lang.lambda.FunctionalDslFactory;
import info.javaspec.lang.lambda.RunSpecsCommand;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

public class StaticCommandFactory implements CommandFactory {
  @Override
  public Command helpCommand(HelpObserver observer, JCommander jCommander) {
    return new HelpCommand(observer, jCommander);
  }

  @Override
  public Command runSpecsCommand(RunObserver observer, List<URL> specClassPath, List<String> classNames) {
//    System.out.println();
//    System.out.printf("specClassPath: <%s>%n", specClassPath);
//    for(int i = 0; i < specClassPath.size(); i++) {
//      URL entry = specClassPath.get(i);
//      System.out.printf("- %s [%s]%n", entry, entry.getClass());
//    }

    ClassLoader specClassLoader = new URLClassLoader(specClassPath.toArray(new URL[0]));
    return new RunSpecsCommand(
      new FunctionalDslFactory(specClassLoader, classNames),
      observer
    );
  }
}
