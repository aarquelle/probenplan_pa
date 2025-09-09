#!/bin/bash
cd "$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
inkscape --export-filename=../probenplan_java/assets/tadu_icon.png ../probenplan_java/assets/tadu_icon.svg
cd ../probenplan_java
mvn install -P linux -f pom.xml
mvn install -P windows -f pom.xml
cd ../installation
cp ../probenplan_java/target/probenplan-linux-jar-with-dependencies.jar probenplan_linux.jar
cp ../probenplan_java/target/probenplan_pa.exe .
cp ../LICENSE LICENSE.txt
zip probenplan_linux.zip run_on_unix.command probenplan_linux.jar LICENSE.txt
#TODO mac