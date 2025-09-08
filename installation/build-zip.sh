#!/bin/bash
cd "$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cp ../probenplan_java/target/*with-dependencies* probenplan.jar
cp ../LICENSE LICENSE.txt
inkscape --export-filename=../probenplan_java/assets/tadu_icon.png ../probenplan_java/assets/tadu_icon.svg
zip probenplan.zip run_on* probenplan.jar LICENSE.txt
