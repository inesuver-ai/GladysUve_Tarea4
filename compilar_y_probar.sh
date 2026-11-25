#!/usr/bin/env bash
# Compila el código fuente y ejecuta todas las pruebas JUnit (Java 17 o superior).
set -e
CP="out/main:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar"
rm -rf out && mkdir -p out/main out/test
javac -encoding UTF-8 -d out/main $(find src -name '*.java')
javac -encoding UTF-8 -cp "$CP" -d out/test $(find test -name '*Test.java')
CLASES=$(cd test && find . -name '*Test.java' | sed 's|^\./||; s|\.java$||; s|/|.|g')
java -cp "out/test:$CP" org.junit.runner.JUnitCore $CLASES
