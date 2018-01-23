# Sparksoniq documentation

Sparksoniq is a Java library that can be used to query JSON files on HDFS, S3 or any other storage layer supported by Spark.

With Sparksoniq, queries can be written in the tailor-made and expressive JSONiq language. Users can write their queries declaratively and start with just a few lines. No need for complex JSON parsing machinery as JSONiq supports the JSON data model natively.

The core of Sparksoniq lies in JSONiq's FLWOR expressions, the semantics of which map beautifully to Spark transformations. Transformations are not exposed as function calls, but are hidden behind FLWOR clauses, giving the user the simplicity of an SQL-like language and the flexibility of heterogeneous, tree-like data.

This documentation provides you with instructions on how to get started, examples of data sets and queries that can be executed locally or on a cluster, links to JSONiq reference and tutorials, notes on the function library implemented so far, and instructions on how to compile Sparksoniq from scratch.

Please note that this is an alpha version. Many bugs are to be expected and we welcome bug reports in the GitHub issues section.
