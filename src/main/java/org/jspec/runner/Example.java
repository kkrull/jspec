package org.jspec.runner;

import java.lang.reflect.Field;

import org.jspec.dsl.It;
import org.junit.runner.Description;

final class Example {
  private final Field behavior;
  
  public Example(Field behavior) {
    this.behavior = behavior;
  }
  
  public String describeBehavior() {
    return behavior.getName();
  }
  
  public Description getDescription() {
    return Description.createTestDescription(behavior.getDeclaringClass(), behavior.getName());
  }
  
  public void run(Object objectDeclaringBehavior) throws Exception {
    behavior.setAccessible(true);
    It thunk = (It)behavior.get(objectDeclaringBehavior);
    thunk.run();
  }
}