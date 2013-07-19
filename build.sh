#!/bin/bash

#build classpath
MYCLASSPATH="."
for i in `ls ./lib/*.jar`
do
  MYCLASSPATH=${MYCLASSPATH}:${i}
done

#remove old class files. (there have to be a better solution. but no network atm)
rm mangaparser/*.class
rm mangaparser/data/*.class
rm mangaparser/junit/*.class

#compile
find -name "*.java" > sources.txt
javac -classpath "${MYCLASSPATH}" @sources.txt

#create jar
jar -cfm MangaParser.jar MANIFEST @sources.txt

rm sources.txt

java -jar MangaParser.jar
