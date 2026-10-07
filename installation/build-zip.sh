#!/bin/bash
cd "$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
echo Converting to PNG...
inkscape --export-filename=../probenplan_java/assets/tadu_icon.png ../probenplan_java/assets/tadu_icon.svg
echo Converting to ICO...
convert ../probenplan_java/assets/tadu_icon.png ../probenplan_java/assets/tadu_icon.ico
cd ../probenplan_java
echo Installing for Linux...
mvn install -P linux -f pom.xml
echo Installing for Windows
mvn install -P windows -f pom.xml
cd ../installation
cp ../probenplan_java/target/probenplan-linux.jar probenplan_linux.jar
cp ../probenplan_java/target/probenplan_pa.exe .
cp ../LICENSE.txt LICENSE.txt
echo Creating ZIP
zip probenplan_linux.zip run_on_unix.command probenplan_linux.jar LICENSE.txt
echo Finished
#TODO mac
