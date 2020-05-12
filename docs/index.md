# Rumble documentation

Rumble is a Java library that can be used to query JSON-like files including JSON, Parquet, AVRO, SVM, CSV, ROOT as well as text files on HDFS, S3, Azure or any other storage layer supported by Spark. It also can read Parquet files, and more input formats will be added.

With Rumble, queries can be written in the tailor-made and expressive JSONiq language. Users can write their queries declaratively and start with just a few lines. No need for complex JSON parsing machinery as JSONiq supports the JSON data model natively.

The core of Rumble lies in JSONiq's FLWOR expressions, the semantics of which map beautifully to DataFrames and Spark SQL. Likewise expression semantics is seamlessly translated to transformations on RDDs. Transformations are not exposed as function calls, but are completely hidden behind JSONiq queries, giving the user the simplicity of an SQL-like language and the flexibility needed to query heterogeneous, tree-like data that does not fit in DataFrames.

This documentation provides you with instructions on how to get started, examples of data sets and queries that can be executed locally or on a cluster, links to JSONiq reference and tutorials, notes on the function library implemented so far, and instructions on how to compile Rumble from scratch.

Please note that this is a (maturing) beta version. We welcome bug reports in the GitHub issues section.
