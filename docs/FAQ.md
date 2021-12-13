# Frequently asked questions and common issues

## Paths with whitespaces

RumbleDB does not currently support paths with a whitespace. Make sure to put your data and modules at paths without whitespaces.

## "Hadoop bin directory does not exist" on Windows

If this happens, you can download winutils.exe to solve the issue as explained (here)[https://phoenixnap.com/kb/install-spark-on-windows-10].

## "java.lang.NoSuchMethodError: com.esotericsoftware.kryo.serializers.FieldSerializer.setIgnoreSyntheticFields" with docker

This is a known issue under investigation. It is related to a version conflict between Kryo 4 and Kryo 5 that occasionally happens on some docker installations.