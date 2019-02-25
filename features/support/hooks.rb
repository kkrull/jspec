Around('@log_commands') do |_scenario, block|
  original_logger = logger

  set_logger ConsoleLogger.new($stdout)
  block.call
  set_logger original_logger
end