# Reading data

Rumble is able to read a variety of formats from a variety of file systems.

We support functions to read JSON, JSON Lines, Parquet, CSV, Text and ROOT files from various storage layers such as S3 and HDFS, Azure blob storage. We run most of our tests on Amazon EMR with S3 or HDFS, as well as locally on the local file system, but we welcome feedback on other setups.

## Supported formats

### JSON

A JSON file containing a single JSON object (or value) can be read with json-doc(). It will not spread access in any way, so that the files should be reasonably small. json-doc() can read JSON files even if the object or value is spread over multiple lines.

```
json-doc("file.json")
```

returns the (single) JSON value read from the supplied JSON file. This will also work for structures spread over multiple lines, as the read is local and not sharded.


### JSON Lines

JSON Lines files are files that have one JSON object (or value) per line. Such files can thus become very large, up to billions or even trillions of JSON objects.

JSON Lines files are read with the json-file() function. json-file() exists in unary and binary. The first parameter specifies the JSON file (or set of JSON files) to read.
The second, optional parameter specifies the minimum number of partitions. It is recommended to use it in a local setup, as the default is only one partition, which does not fully use the parallelism. If the input is on HDFS, then blocks are taken as splits by default. This is also similar to Spark's textFile().

Example of usage:

```
for $my-json in json-file("hdfs://host:port/directory/file.json")
where $my-json.property eq "some value"
return $my-json
```

If a host and port are set:

```
for $my-json in json-file("/absolute/directory/file.json")
where $my-json.property eq "some value"
return $my-json
```

For a set of files:

```
for $my-json in json-file("/absolute/directory/file-*.json")
where $my-json.property eq "some value"
return $my-json
```

If a working directory is set:

```
for $my-json in json-file("file.json")
where $my-json.property eq "some value"
return $my-json
```

Several files or whole directories can be read with the same pattern syntax as in Spark.


```
for $my-json in json-file("*.json")
where $my-json.property eq "some value"
return $my-json
```

In some cases, JSON Lines files are highly structured, meaning that all objects have the same fields and these fields are associated with values with the same types. In this case, Rumble will be faster navigating such files if you open them with the function structured-json-file().

structured-json-file() parses one or more json files that follow [JSON-lines](http://jsonlines.org/) format and returns a sequence of objects. This enables better performance with fully structured data and is recommended to use only when such data is available.

Warning: when the data has multiple types for the same field, this field and contained values will be treated as strings. This is also similar to Spark's spark.read.json().

Example of usage:

```
for $my-structured-json in structured-json-file("hdfs://host:port/directory/structured-file.json")
where $my-structured-json.property eq "some value"
return $my-structured-json
```

### Text

Text files can be read into a sequence of string items, one string per line. Rumble can open files that have billions or potentially even trillions of lines with the function text-file().

text-file() exists in unary and binary. The first parameter specifies the text file (or set of text files) to read and return as a sequence of strings.

The second, optional parameter specifies the minimum number of partitions. It is recommended to use it in a local setup, as the default is only one partition, which does not fully use the parallelism. If the input is on HDFS, then blocks are taken as splits by default. This is also similar to Spark's textFile().

Example of usage:

```
count(
  for $my-string in text-file("hdfs://host:port/directory/file.txt")
  for $token in tokenize($my-string, ";")
  where $token eq "some value"
  return $token
)
```

Several files or whole directories can be read with the same pattern syntax as in Spark.

(Also see examples for json-file for host and port, sets of files and working directory).

### Parquet

Parquet files can be open with the function parquet-file().

Parses one or more parquet files and returns a sequence of objects. This is also similar to Spark's spark.read.parquet()

```
for $my-object in parquet-file("file.parquet")
where $my-object.property eq "some value"
return $my-json
```

Several files or whole directories can be read with the same pattern syntax as in Spark.

```
for $my-object in parquet-file("*.parquet")
where $my-object.property eq "some value"
return $my-json
```

### CSV

CSV files can be open with the function csv-file().

Parses one or more csv files and returns a sequence of objects. This is also similar to Spark's spark.read.csv()

```
for $i in csv-file("file.csv")
where $i._c0 eq "some value"
return $i
```

Several files or whole directories can be read with the same pattern syntax as in Spark.

```
for $i in csv-file("*.csv")
where $i._c0 eq "some value"
return $i
```

Options can be given in the form of a JSON object. All available options can be found in the [Spark documentation](https://spark.apache.org/docs/latest/api/java/org/apache/spark/sql/DataFrameReader.html#csv-java.lang.String...-)

```
for $i in csv-file("file.csv", {"header": true, "inferSchema": true})
where $i.key eq "some value"
return $i
```

### ROOT

ROOT files can be open with the function root-file(). The second parameter specifies the path within the ROOT files (a ROOT file is like a mini-file system of its own). It is often `Events` or `tree`.


```
for $i in root-file("events.root", "Events")
where $i._c0 eq "some value"
return $i
```

## Creating your own big sequence

The function parallelize() can be used to create, on the fly, a big sequence of items in such a way that Rumble can spread its querying across cores and machines.

This function behaves like the Spark parallelize() you are familiar with and sends a large sequence to the cluster.
The rest of the FLWOR expression is then evaluated with Spark transformations on the cluster.

```
for $i in parallelize(1 to 1000000)
where $i mod 1000 eq 0
return $i
```

There is also be a second, optional parameter that specifies the minimum number of partitions.

```
for $i in parallelize(1 to 1000000, 100)
where $i mod 1000 eq 0
return $i
```

## Supported file systems

As a general rule of thumb, Rumble can read from any file system that Spark can read from. The file system is inferred from the scheme used in the path used in any of the functions described above.

Note that the scheme is optional, in which case the default file system as configured in Hadoop and Spark is used.
A relative path can also be provided, in which case the working directory (including its file system) as configured is used.

### Local file system

The scheme for the local file system is `file://`. Pay attention to the fact that for reading an absolute path, a third slash will follow the scheme.

Example:

```
file:///home/user/file.json
```

Warning! If you try to open a file from the local file system on a cluster of several machines, this might fail as the file is only on the machine that you are connected to. You need to pass additional parameters to `spark-submit` to make sure that any files read locally will be copied over to all machines.

If you use `spark-submit` locally, however, this will work out of the box, but we recommend specifying a number of partitions to avoid reading the file as a single partition.

### HDFS

The scheme for the Hadoop Distributed File System is `hdfs://`. A host and port should also be specified.

Example:

```
hdfs://www.example.com:8021/user/hadoop/file.json
```

### S3

There are three schemes for reading from S3: `s3://`, `s3n://` and `s3a://`.

Examples:

```
s3://my-bucket/directory/file.json
s3n://my-bucket/directory/file.json
s3a://my-bucket/directory/file.json
```

If you are on an Amazon EMR cluster, `s3://` is straightforward to use and will automatically authenticate. For more details on how to set up your environment to read from S3 and which scheme is most appropriate, we refer to the Amazon S3 documentation.


### Azure blob storage

The scheme for Azure blob storage is `wasb://`.

Example:

```
wasb://mycontainer@myaccount.blob.core.windows.net/directory/file.json
```
