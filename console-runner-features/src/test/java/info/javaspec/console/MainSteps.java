package info.javaspec.console;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import info.javaspec.MockSpecReporter;

/** Steps observing what happens in the overall process of running specs, from *within* the same process */
public class MainSteps {
  private RunsMain runMain;
  private MockSpecReporter mockReporter;
  private MockExitHandler system;

  private Verification declaredSpecsRan;
  private Verification expectedResultsReported;

  public MainSteps() {
    this.runMain = () -> { throw new UnsupportedOperationException("runMain not defined"); };
    this.declaredSpecsRan = () -> { throw new UnsupportedOperationException("Verification not defined"); };
    this.expectedResultsReported = () -> { throw new UnsupportedOperationException("Verification not defined"); };
  }

  @Given("^I have a JavaSpec class runner$")
  public void iHaveAClassRunner() throws Exception {
    this.mockReporter = new MockSpecReporter();
    this.system = new MockExitHandler();
  }

  @Given("^I have a Java class that defines a suite of lambda specs$")
  public void iHaveAJavaClassWithASuiteOfLambdaSpecs() throws Exception {
    this.runMain = () -> {
      MainStepsOneOfEach.reset();
      Main.main(this.mockReporter, this.system, MainStepsOneOfEach.class.getCanonicalName());
    };

    this.declaredSpecsRan = MainStepsOneOfEach::specsShouldHaveRun;
    this.expectedResultsReported = () -> {
      this.mockReporter.specShouldHavePassed("passes");
      this.mockReporter.specShouldHaveFailed("fails");
    };
  }

  @Given("^I have a Java class that defines a suite of passing lambda specs$")
  public void iHaveAJavaClassThatDefinesASuiteOfPassingLambdaSpecs() throws Exception {
    this.runMain = () -> Main.main(this.mockReporter, this.system, MainStepsOnePasses.class.getCanonicalName());
  }

  @Given("^I have a Java class that defines a suite of 1 or more failing lambda specs$")
  public void iHaveASuiteWithFailingSpecs() throws Exception {
    this.runMain = () -> Main.main(this.mockReporter, this.system, MainStepsOneFails.class.getCanonicalName());
  }

  @When("^I run the specs in that class$")
  public void whenRunningSpecs() throws Exception {
    this.runMain.run();
  }

  @Then("^The runner should run the specs defined in that class$")
  public void theRunnerShouldRunSpecs() throws Exception {
    this.declaredSpecsRan.verify();
  }

  @Then("^The runner should indicate which specs passed and failed$")
  public void theRunnerShouldIndicateWhichSpecsPassedAndFailed() throws Exception {
    this.expectedResultsReported.verify();
  }

  @Then("^The runner should indicate that 1 or more specs have failed$")
  public void theReporterShouldIndicateFailingStatus() throws Exception {
    this.system.exitShouldHaveReceived(1);
  }

  @Then("^The runner should indicate that all specs passed$")
  public void theReporterShouldIndicateThatAllSpecsPassed() throws Exception {
    this.system.exitShouldHaveReceived(0);
  }

  interface RunsMain {
    void run();
  }
}