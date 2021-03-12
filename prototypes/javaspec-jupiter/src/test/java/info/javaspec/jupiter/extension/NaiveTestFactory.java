package info.javaspec.jupiter.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import java.lang.reflect.InvocationTargetException;

public class NaiveTestFactory implements TestInstanceFactory {
  private final Class<?> testClass;

  public NaiveTestFactory(Class<?> testClass) {
    this.testClass = testClass;
  }

  @Override
  public Object createTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext extensionContext)
    throws TestInstantiationException {
    //TODO KDK: How can this (or any) extension create multiple test instances?  This just creates one.
    try {
      return this.testClass.getDeclaredConstructor().newInstance();
    } catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new TestInstantiationException("Failed to instantiate test class", e);
    }
  }
}
