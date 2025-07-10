# RumbleDB documentation

RumbleDB is a querying engine that allows you to query your large, messy datasets with ease and productivity. It covers the entire data pipeline: clean up, structure, normalize, validate, convert to an efficient binary format, and feed it right into Machine Learning estimators and models, all within the JSONiq language.

RumbleDB supports JSON-like datasets including JSON, JSON Lines, Parquet, Avro, SVM, CSV, ROOT as well as text files, of any size from kB to at least the two-digit TB range (we have not found the limit yet).

RumbleDB is both good at handling small amounts of data on your laptop (in which case it simply runs locally and efficiently in a single-thread) as well as large amounts of data by spreading computations on your laptop cores, or onto a large cluster (in which case it leverages Spark automagically).

RumbleDB can also be used to easily and efficiently convert data from a format to another, including from JSON to Parquet thanks to JSound validation.

It runs on many local or distributed filesystems such as HDFS, S3, Azure blob storage, and HTTP (read-only), and of course your local drive as well. You can use any of these file systems to store your datasets, but also to store and share your queries and functions as library modules with other users, worldwide or within your institution, who can import them with just one line of code. You can also output the results of your query or the log to these filesystems (as long as you have write access).

With RumbleDB, queries can be written in the tailor-made and expressive JSONiq language. Users can write their queries declaratively and start with just a few lines. No need for complex JSON parsing machinery as JSONiq supports the JSON data model natively.

The core of RumbleDB lies in JSONiq's FLWOR expressions, the semantics of which map beautifully to DataFrames and Spark SQL. Likewise expression semantics is seamlessly translated to transformations on RDDs or DataFrames, depending on whether a structure is recognized or not. Transformations are not exposed as function calls, but are completely hidden behind JSONiq queries, giving the user the simplicity of an SQL-like language and the flexibility needed to query heterogeneous, tree-like data that does not fit in DataFrames.

This documentation provides you with instructions on how to get started, examples of data sets and queries that can be executed locally or on a cluster, links to JSONiq reference and tutorials, notes on the function library implemented so far, and instructions on how to compile RumbleDB from scratch.

Please note that this is a (maturing) beta version. We welcome bug reports in the GitHub issues section.
