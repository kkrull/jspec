#!/usr/bin/env bash

set -e

fswatch -0 src/FizzBuzzSpecs.java | xargs -0 -n1 -I {} make
