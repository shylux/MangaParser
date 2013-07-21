#!/bin/bash

#build classpath
MYCLASSPATH="."
for i in `ls ./lib/*.jar`
do
  MYCLASSPATH=${MYCLASSPATH}:${i}
done

#compile
find -name "*.java" > sources.txt
javac -classpath "${MYCLASSPATH}" @sources.txt

#create jar
jar -cfm MangaParser.jar MANIFEST @sources.txt

rm sources.txt

java -jar MangaParser.jar
