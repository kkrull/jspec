## Steps focused on ConsoleRunner's behavior _as a process_

Given("I have a JavaSpec runner for the console") do
  spec_runner_helper.runner_class = 'info.javaspec.console.Runner'
end

Given("I have a Java class that defines a suite of lambda specs") do
  spec_runner_helper.spec_class = 'info.javaspec.example.OneOfEachResult'
end

Given("I have a Java class that defines a suite of passing lambda specs") do
  spec_runner_helper.spec_class = 'info.javaspec.example.AllPass'
end

Given("I have a Java class that defines a suite of 1 or more failing lambda specs") do
  spec_runner_helper.spec_class = 'info.javaspec.example.OneFails'
end

When("I run the specs in that class") do
  spec_runner_helper.class_files_should_exist
  spec_runner_helper.run! logger
end

Then("The runner should run the specs defined in that class") do
  expect(spec_runner_helper.runner_output).to match(/^passes/)
  expect(spec_runner_helper.runner_output).to match(/^fails/)
end

Then("The runner should indicate whether each spec passed or failed") do
  expect(spec_runner_helper.runner_output).to match(/says hello: PASS/)
end

Then("The runner should indicate whether all specs passed, or any failed") do
  expect(spec_runner_helper.exit_status).to eq(0)
  expect(spec_runner_helper.runner_output).to include("Passed: 1\tFailed: 0\tTotal: 1")
end

Then("The runner should indicate which specs passed and failed") do
  expect(spec_runner_helper.runner_output).to match(/^passes: PASS$/)
  expect(spec_runner_helper.runner_output).to match(/^fails: FAIL$/)
end

Then("The runner should indicate that all specs passed") do
  expect(spec_runner_helper.exit_status).to eq(0)
  expect(spec_runner_helper.runner_output).to include("Passed: 1\tFailed: 0\tTotal: 1")
end

Then("The runner should indicate that 1 or more specs have failed") do
  expect(spec_runner_helper.exit_status).to eq(1)
  expect(spec_runner_helper.runner_output).to include("Passed: 0\tFailed: 1\tTotal: 1")
end
