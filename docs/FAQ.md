# Frequently asked questions and common issues

## Paths with whitespaces

RumbleDB does not currently support paths with a whitespace. Make sure to put your data and modules at paths without whitespaces.

## "Hadoop bin directory does not exist" on Windows

If this happens, you can download winutils.exe to solve the issue as explained [here](https://phoenixnap.com/kb/install-spark-on-windows-10).

## "java.lang.NoSuchMethodError: com.esotericsoftware.kryo.serializers. FieldSerializer.setIgnoreSyntheticFields" with docker

This is a known issue under investigation. It is related to a version conflict between Kryo 4 and Kryo 5 that occasionally happens on some docker installations. We recommend trying a local installation instead, as described in the Getting Started section.

## Java version

A very common issue leading to some errors is using the wrong Java version. With Spark 2+, only Java 8 is supported. With Spark 3+, Java 8 or 11 are supported but no other version. You should make sure in particular you are not using a more recent Java version. Multiple Java versions can normally co-habit on the same machine but you need to make sure to set the JAVA_HOME variable appropriately.
