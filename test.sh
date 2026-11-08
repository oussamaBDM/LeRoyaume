#!/bin/bash
# compile le jeu puis les tests, et lance les tests depuis la racine du projet
javac -d bin src/main/java/*.java && javac -cp bin -d bin src/test/java/*.java || exit 1
java -cp bin TestMain
echo
java -cp bin TestRoi
