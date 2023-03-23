# Frequently asked questions and common issues

## Out of memory error

By default, the memory allocated is limited. This depends on whether you run RumbleDB with the standalone jar or as the thin jar in a Spark environment.

If you run RumbleDB with a standalone jar, then your laptop will allocate by default one quarter of your total working memory. You can check this with:

    java -XX:+PrintFlagsFinal -version | grep -iE 'MaxHeapSize'   

In order to increase the memory, you can use `-Xmx10g` (for 10 GB, but you can use any other value):

    java -jar -Xmx10g rumbledb-1.20.0-standalone.jar ...
    
If you run RumbleDB on your laptop (or a single machine) with the thin jar, then by default this is limited to around 2 GB, and you can change this with `--driver-memory`:

    spark-submit --driver-memory 10G rumbledb-1.20.0-for-spark-3.1.jar ...
    
If you run RumbleDB on a cluster, then the memory needs to be allocated to the executors, not the driver:

    spark-submit --executor-memory 10G rumbledb-1.20.0-for-spark-3.1.jar ...
    
Setting things up on a cluster requires more thinking because setting the executor memory should be done in conjunction with setting the total number of executors and the number of cores per executor. This highly depends on your cluster hardware.

## Paths with whitespaces

RumbleDB does not currently support paths with a whitespace. Make sure to put your data and modules at paths without whitespaces.

## "Hadoop bin directory does not exist" on Windows

If this happens, you can download winutils.exe to solve the issue as explained [here](https://phoenixnap.com/kb/install-spark-on-windows-10).

## "java.lang.NoSuchMethodError: com.esotericsoftware.kryo.serializers. FieldSerializer.setIgnoreSyntheticFields" with docker

This is a known issue under investigation. It is related to a version conflict between Kryo 4 and Kryo 5 that occasionally happens on some docker installations. We recommend trying a local installation instead, as described in the Getting Started section.

## Java version

A very common issue leading to some errors is using the wrong Java version. With Spark 2+, only Java 8 is supported. With Spark 3+, Java 8 or 11 are supported but no other version. You should make sure in particular you are not using a more recent Java version. Multiple Java versions can normally co-habit on the same machine but you need to make sure to set the JAVA_HOME variable appropriately.
