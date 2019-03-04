package info.javaspec.lang.lambda;

import info.javaspec.Spec;
import info.javaspec.Suite;

import java.util.Optional;
import java.util.Stack;

/** Groups recently-declared specs into a suite of specs that can be run together */
final class DeclarationScope {
  private final Stack<WritableSuite> collections;

  public DeclarationScope() {
    this.collections = new Stack<>();
    this.collections.push(new RootSuite());
  }

  public void declareSpecsFor(String subject, BehaviorDeclaration describeBehavior) {
    SequentialSuite newSubjectCollection = new SequentialSuite(subject);
    leafCollection().get().addSubCollection(newSubjectCollection); //Add the child collection in line with any other declared specs
    this.collections.push(newSubjectCollection); //Push on to the stack in case there are nested describes
    describeBehavior.declareSpecs();
    this.collections.pop();
  }

  public void createSpec(String intendedBehavior, BehaviorVerification verification) {
    Spec spec = new DescriptiveSpec(intendedBehavior, verification);
    subjectCollection()
      .orElseThrow(() -> Exceptions.NoSubjectDefined.forSpec(intendedBehavior))
      .addSpec(spec);
  }

  public Suite createRootCollection() {
    Suite rootCollection = this.collections.pop();
    if(!this.collections.isEmpty())
      throw new IllegalStateException("Spec declaration ended prematurely");

    return rootCollection;
  }

  private Optional<WritableSuite> subjectCollection() {
    return this.leafCollection()
      .filter(x -> !RootSuite.class.equals(x.getClass()));
  }

  private Optional<WritableSuite> leafCollection() {
    return this.collections.empty()
      ? Optional.empty()
      : Optional.of(this.collections.peek());
  }
}