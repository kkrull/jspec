JC = javac
JCCLASSPATH = ../lambdas-and-strings/lambda-api/build/libs/lambda-api-2.0.0-SNAPSHOT.jar:/Users/kkrull/.m2/repository/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar
JFLAGS = \
  -d out/production/kata \
	-cp $(JCCLASSPATH)

RUNFLAGS = \
	--reporter=plaintext \
	--spec-classpath=/Users/kkrull/.m2/repository/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar:out/production/kata

default: out/production/kata/FizzBuzzSpecs.class run

clean:
	rm -Rf out

out/production/kata/FizzBuzzSpecs.class out/production/kata/out/production/kata/FizzBuzzSpecs$FizzBuzz.class &: src/FizzBuzzSpecs.java
	mkdir -p out/production/kata
	$(JC) $(JFLAGS) src/FizzBuzzSpecs.java

run:
	javaspec run $(RUNFLAGS) FizzBuzzSpecs
