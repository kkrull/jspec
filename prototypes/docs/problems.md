# Problem Space

## Writing Specs
### Syntax for defining specs

* [x] Descriptions of expected behavior(s): `it`.
* [x] Procedures to verify those expectations `it`.
* [x] Description of what (class, method, or function) is being tested: `describe`.
* [x] Description of any particular circumstances, during which those expectations apply: `context`.
* [x] Disabled spec, that should not be run: `disabled`.
* [x] Pending spec, that lacks a verification: `pending`.
* [ ] Declaration of reused variables.
* [ ] Reusing common setup code that runs before each spec in a container.
* [ ] Reusing common teardown code that runs after each spec in a container.

`StaticMethodSyntax` shows concise JavaSpec syntax by using static imports for JavaSpec methods.  It's more concise–and
there are fewer opportunities to mix up scope–than with passing scope/context parameters back to the lambdas.

The syntax for `disabled` is nice to write, but running it is quite misleading.  Could JavaSpec create the `DyanmicTest`
as always, but also add a `TestFilter` to ignore it?

How could a type-safe subject be instantiated in each spec?

1. Extend a base class `JavaSpecs<S>` that has parameterized subject methods: `SubjectFromBaseClassSpecs`.
1. Immutable objects can just be declared once in a `describe` block and shared at runtime.
1. Don't make it typesafe - pass in the `.class` you want and cast it before returning.
1. Extendable syntax for static methods?

    ```java
    //I don't think static methods can be extended (with new parameters)
    class GreeterSpecs extends JavaSpec<Greeter> { ... }
    class JavaSpec<S extends Object> { ... }
    ```
1. Same, but use an instance of `JavaSpec` instead of static methods.


### Where in a spec class to use this syntax, to declare specs

* [ ] Jupiter-tagged test factory method
* [ ] Implement a method from a `JavaSpec` base class, that is wired up to a Jupiter-tagged test factory method
* [ ] Constructor
* [ ] Instance initializer
* [ ] Static initializer (ew)


----
## Running Specs
### How to run specs, in IntelliJ

* [ ] Gradle test runner: Does not show `@DisplayName`, for regular Jupiter syntax.
* [ ] IntelliJJ test runner: Works for `@DisplayName` in Jupiter syntax, and working so far for `DynamicTest`.


### How to run specs, on the command line

* [ ] Use the `junit-jupiter-standalone` jar
* [ ] Make our own CLI, for JavaSpec (hard)


### How to run specs, in CI

> Is running in CI _exactly_ the same as regular, command-line usage, or are could there be subtle differences?


### How to report results

* [ ] Finding exceptions that happen in the production code and exceptions that happen in the test code, and tracing
  back from the stack trace to both.
* [ ] On the command-line: The Jupiter standalone jar would be best
* [ ] When running on the command-line, during CI
* [ ] When running in Gradle: Uhh...use the built-in test reporter?  Compatible with custom reporters? Write our own
  custom reporter?
* [ ] When running in IntelliJ: IntelliJ Test Runner seems to be handling Jupiter syntax ok, so far.


### How to trace back to failed assertions and/or failures in production code

* [ ] Pass a `URI` to the `DynamicNode` factory methods, with a URI to the related spec syntax.


----
## Interface with `jupiter-engine`
### How to get `junit-jupiter-engine` to ask JavaSpec for `DynamicNodes` (tests and containers)

* [ ] Tag the factory method with `@TestFactory`
* [ ] Tag the test/spec class with an extension: Is there an extension for test factories?
* [ ] Call a Jupiter registration method?  Does it need to be tagged with something else?


### How to represent specs, as Jupiter tests

* [ ] `DynamicNode` base class for all test-related constructs
* [ ] `Stream<DynamicNode>` for any number of tests and/or containers
* [ ] `DynamicTest` for test descriptions and verification procedures
* [ ] `DynamicContainer` for what is being tested and contextual circumstances


### Where to store specs and/or Jupiter test objects (`DynamicNode`)

* [ ] JavaSpec singleton: Might work as long as only 1 spec class is declaring at a time.
* [ ] JavaSpec "class-local": Something like `ThreadLocal`, but tied to the calling test class instead of to a thread.