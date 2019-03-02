package info.javaspec.lang.lambda;

class Exceptions {
  static final class DeclarationAlreadyStarted extends IllegalStateException {
    DeclarationAlreadyStarted() {
      super("Declaration has already been started.  Please call FunctionalDslDeclaration::endDeclaration on the prior declaration, if a brand new root suite is desired.");
    }
  }

  static final class DeclarationNotStarted extends IllegalStateException {
    DeclarationNotStarted() {
      super("No declaration has been started.  Has FunctionalDslDeclaration::beginDeclaration been called?");
    }
  }

  static final class NoSubjectDefined extends IllegalStateException {
    static NoSubjectDefined forSpec(String intendedBehavior) {
      String message = String.format("No subject defined for spec: %s", intendedBehavior);
      return new NoSubjectDefined(message);
    }

    private NoSubjectDefined(String message) {
      super(message);
    }
  }

  static final class SpecDeclarationFailed extends RuntimeException {
    static SpecDeclarationFailed whenInstantiating(Class<?> specClass, Exception cause) {
      return new SpecDeclarationFailed(
        String.format("Failed to instantiate class %s, to declare specs", specClass.getName()),
        cause);
    }

    private SpecDeclarationFailed(String message, Exception cause) {
      super(message, cause);
    }
  }
}
