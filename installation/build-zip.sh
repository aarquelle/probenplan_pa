#!/bin/bash
cd "$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cp ../probenplan/target/*with-dependencies* probenplan.jar
cp ../LICENSE LICENSE.txt
zip probenplan.zip run_on* probenplan.jar LICENSE.txt
