JC = javac
MAVENREPO = $(HOME)/.m2/repository

SPECCLASSDIR = out/production/kata
COMPILECLASSPATH = ../lambdas-and-strings/lambda-api/build/libs/lambda-api-2.0.0-SNAPSHOT.jar:$(MAVENREPO)/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar
JFLAGS = \
  -d $(SPECCLASSDIR) \
	-cp $(COMPILECLASSPATH)

RUNFLAGS = \
	--reporter=plaintext \
	--spec-classpath=$(SPECCLASSDIR):$(MAVENREPO)/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar

#Exclude nested class targets, which causes re-compile
default: $(SPECCLASSDIR)/FizzBuzzSpecs.class run

clean:
	rm -Rf out

$(SPECCLASSDIR)/FizzBuzzSpecs.class $(SPECCLASSDIR)/FizzBuzzSpecs$FizzBuzz.class &: src/FizzBuzzSpecs.java
	mkdir -p $(SPECCLASSDIR)
	$(JC) $(JFLAGS) src/FizzBuzzSpecs.java

run:
	javaspec run $(RUNFLAGS) FizzBuzzSpecs
