JCC = javac
JFLAGS = -d out/production/kata -cp ../lambdas-and-strings/lambda-api/build/libs/lambda-api-2.0.0-SNAPSHOT.jar:/Users/kkrull/.m2/repository/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar

default: out/production/kata/FizzBuzzSpecs.class run

clean:
	rm -Rf out

out/production/kata/FizzBuzzSpecs.class: src/FizzBuzzSpecs.java
	mkdir -p out/production/kata
	$(JCC) $(JFLAGS) src/FizzBuzzSpecs.java

out/production/kata/out/production/kata/FizzBuzzSpecs$FizzBuzz.class: src/FizzBuzzSpecs.java
	mkdir -p out/production/kata
	$(JCC) $(JFLAGS) src/FizzBuzzSpecs.java

run:
	javaspec run --reporter=plaintext --spec-classpath=/Users/kkrull/.m2/repository/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar:out/production/kata FizzBuzzSpecs
