JAVAC = javac
MAVEN_REPO = $(HOME)/.m2/repository

SPEC_CLASS_DIR = out/production/kata
COMPILE_CLASSPATH = ../lambdas-and-strings/lambda-api/build/libs/lambda-api-2.0.0-SNAPSHOT.jar:$(MAVEN_REPO)/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar
JAVAC_FLAGS = \
  -d $(SPEC_CLASS_DIR) \
	-cp $(COMPILE_CLASSPATH)

RUN_FLAGS = \
	--reporter=plaintext \
	--spec-classpath=$(SPEC_CLASS_DIR):$(MAVEN_REPO)/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar

#Exclude nested class targets, which causes re-compile
default: $(SPEC_CLASS_DIR)/FizzBuzzSpecs.class run

clean:
	rm -Rf out

$(SPEC_CLASS_DIR)/FizzBuzzSpecs.class $(SPEC_CLASS_DIR)/FizzBuzzSpecs$FizzBuzz.class &: src/FizzBuzzSpecs.java
	mkdir -p $(SPEC_CLASS_DIR)
	$(JAVAC) $(JAVAC_FLAGS) src/FizzBuzzSpecs.java

run:
	javaspec run $(RUN_FLAGS) FizzBuzzSpecs
