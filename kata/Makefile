JAVAC = javac
MAVEN_REPO = $(HOME)/.m2/repository

SPEC_CLASS_DIR = out/production/kata
COMPILE_JARS = \
	src/: \
	../lambdas-and-strings/lambda-api/build/libs/lambda-api-2.0.0-SNAPSHOT.jar: \
	$(MAVEN_REPO)/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar

COMPILE_CLASSPATH = $(subst : ,:,$(COMPILE_JARS))
JAVAC_FLAGS = \
	-cp $(COMPILE_CLASSPATH) \
  -d $(SPEC_CLASS_DIR)

RUN_JARS = \
	$(SPEC_CLASS_DIR): \
	$(MAVEN_REPO)/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar
RUN_FLAGS = \
	--reporter=plaintext \
	--spec-classpath=$(subst : ,:,$(RUN_JARS))

#Exclude nested class targets, which causes re-compile
default: $(SPEC_CLASS_DIR)/FizzBuzz.class $(SPEC_CLASS_DIR)/FizzBuzzSpecs.class run

.PHONY: clean
clean:
	rm -Rf out

$(SPEC_CLASS_DIR)/FizzBuzz.class: src/FizzBuzz.java
	@mkdir -p $(SPEC_CLASS_DIR)
	$(JAVAC) $(JAVAC_FLAGS) src/FizzBuzz.java

$(SPEC_CLASS_DIR)/FizzBuzzSpecs.class: src/FizzBuzzSpecs.java
	@mkdir -p $(SPEC_CLASS_DIR)
	$(JAVAC) $(JAVAC_FLAGS) src/FizzBuzzSpecs.java

.PHONY: run
run:
	javaspec run $(RUN_FLAGS) FizzBuzzSpecs

.PHONY: watch
watch:default
	fswatch -0 src | xargs -0 -n1 -I{} make
