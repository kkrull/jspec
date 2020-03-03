JC = javac
MAVENREPO = $(HOME)/.m2/repository

JOUT = out/production/kata
JCCLASSPATH = ../lambdas-and-strings/lambda-api/build/libs/lambda-api-2.0.0-SNAPSHOT.jar:$(MAVENREPO)/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar
JFLAGS = \
  -d $(JOUT) \
	-cp $(JCCLASSPATH)

RUNFLAGS = \
	--reporter=plaintext \
	--spec-classpath=$(MAVENREPO)/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar:$(JOUT)

default: $(JOUT)/FizzBuzzSpecs.class run

clean:
	rm -Rf out

$(JOUT)/FizzBuzzSpecs.class $(JOUT)/FizzBuzzSpecs$FizzBuzz.class &: src/FizzBuzzSpecs.java
	mkdir -p $(JOUT)
	$(JC) $(JFLAGS) src/FizzBuzzSpecs.java

run:
	javaspec run $(RUNFLAGS) FizzBuzzSpecs
