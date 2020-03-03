#!/usr/bin/env bash

set -e

rm -Rf out
mkdir -p out/production/kata
javac -d out/production/kata -cp ../lambdas-and-strings/lambda-api/build/libs/lambda-api-2.0.0-SNAPSHOT.jar:/Users/kkrull/.m2/repository/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar src/FizzBuzzSpecs.java 
javaspec run --reporter=plaintext --spec-classpath=/Users/kkrull/.m2/repository/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar:out/production/kata FizzBuzzSpecs

