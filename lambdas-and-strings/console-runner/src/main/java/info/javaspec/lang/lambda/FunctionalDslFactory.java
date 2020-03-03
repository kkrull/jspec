package info.javaspec.lang.lambda;

import info.javaspec.SpecCollection;
import info.javaspec.lang.lambda.Exceptions.SpecDeclarationFailed;

import java.util.List;

public class FunctionalDslFactory implements SpecCollectionFactory {
  private final ClassLoader loader;
  private final List<String> classNames;

  public FunctionalDslFactory(ClassLoader loader, List<String> classNames) {
    this.loader = loader;
    this.classNames = classNames;
  }

  /*
  $ javaspec run --reporter=plaintext --spec-classpath=/Users/kkrull/.m2/repository/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar:out/production/kata FizzBuzzSpecs
[StaticCommandFactory] urls: [file:/Users/kkrull/.m2/repository/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar, file:/Users/kkrull/git/jvm/javaspec/kata/out/production/kata/]
Loading spec class: FizzBuzzSpecs
FizzBuzz
  #encode
  * translates 1 as-is: FAIL [1]

Specs failed:
[1] java.lang.AssertionError:
Expected: "1"
     but: was null
        at org.hamcrest.MatcherAssert.assertThat(MatcherAssert.java:20)
        at org.hamcrest.MatcherAssert.assertThat(MatcherAssert.java:6)
        at FizzBuzzSpecs.lambda$null$0(FizzBuzzSpecs.java:13)
        at info.javaspec.lang.lambda.DescriptiveSpec.run(DescriptiveSpec.java:24)
        at info.javaspec.lang.lambda.SequentialCollection.lambda$runSpecs$0(SequentialCollection.java:50)
        at java.lang.Iterable.forEach(Iterable.java:75)
        at info.javaspec.lang.lambda.SequentialCollection.runSpecs(SequentialCollection.java:50)
        at info.javaspec.lang.lambda.SequentialCollection.lambda$runSpecs$1(SequentialCollection.java:51)
        at java.lang.Iterable.forEach(Iterable.java:75)
        at info.javaspec.lang.lambda.SequentialCollection.runSpecs(SequentialCollection.java:51)
        at info.javaspec.lang.lambda.RootCollection.lambda$runSpecs$0(RootCollection.java:37)
        at java.util.ArrayList.forEach(ArrayList.java:1257)
        at info.javaspec.lang.lambda.RootCollection.runSpecs(RootCollection.java:37)
        at info.javaspec.lang.lambda.RunSpecsCommand.run(RunSpecsCommand.java:27)
        at info.javaspec.console.Main.runCommand(Main.java:44)
        at info.javaspec.console.Main.main(Main.java:35)
        at info.javaspec.console.Main.main(Main.java:15)

[Testing complete] Passed: 0, Failed: 1, Total: 1
   */

  @Override
  public SpecCollection declareSpecs() {
    FunctionalDsl.openScope();
    for(String className : this.classNames) {
      System.out.printf("Loading spec class: %s%n", className);
      Class<?> specClass;
      try {
        specClass = Class.forName(className, true, this.loader);
      } catch(ClassNotFoundException | ExceptionInInitializerError e) {
        throw SpecDeclarationFailed.whenLoading(className, e);
      }

      try {
        specClass.newInstance();
      } catch(Exception e) {
        throw SpecDeclarationFailed.whenInstantiating(specClass, e);
      }
    }

    return FunctionalDsl.closeScope();
  }
}
