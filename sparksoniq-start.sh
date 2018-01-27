#!/usr/bin/env bash

if [[ ! -e target/jsoniq-spark-app-0.9.2-jar-with-dependencies.jar ]];
then
echo "jar not present. Building..."
mvn clean compile assembly:single
fi


spark-submit --class sparksoniq.ShellStart --conf spark.executor.extraClassPath=../lib/* --conf spark.driver.extraClassPath=../lib/* target/jsoniq-spark-app-0.9.2-jar-with-dependencies.jar --master local[2] --deploy-mode client