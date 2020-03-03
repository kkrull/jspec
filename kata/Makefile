default: out/production/kata/FizzBuzzSpecs.class

clean:
	rm -Rf out

out/production/kata/FizzBuzzSpecs.class: src/FizzBuzzSpecs.java
	mkdir -p out/production/kata
	javac -d out/production/kata -cp ../lambdas-and-strings/lambda-api/build/libs/lambda-api-2.0.0-SNAPSHOT.jar:/Users/kkrull/.m2/repository/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar src/FizzBuzzSpecs.java 

run:
	javaspec run --reporter=plaintext --spec-classpath=/Users/kkrull/.m2/repository/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar:out/production/kata FizzBuzzSpecs
