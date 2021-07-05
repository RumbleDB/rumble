# RumbleDB parameters

The parameters that can be used on the command line as well as on the planned HTTP server are shown below.


|  Shell parameter | HTTP parameter  | example values  | Semantics  |
|---|---|---|---|
| --shell  | N/A  |  yes, no |  yes runs the interactive shell. No executes a query specified with --query-path |
| --query-path  | query-path  | file:///folder/file.jq  | A JSONiq query file to read from (from any file system, even the Web!).  |
| --output-path  |  output-path | file:///folder/output  | Where to output to (if the output is large, it will create a sharded directory, otherwise it will create a file) |
| --output-format  |  N/A | json, csv, avro, parquet, or any other format supported by Spark | An output format to use for the output. Formats other than json can only be output if the query outputs a highly structured sequence of objects (you can nest your query in an annotate() call to specify a schema if it does not). |
| --output-format-option:foo  |  N/A | bar | Options to further specify the output format (example: separator character for CSV, compression format...) |
| --overwrite  |  overwrite | yes, no | Whether to overwrite to --output-path. No throws an error if the output file/folder exists. |
| --materialization-cap |  materialization-cap | 200 | A cap on the maximum number of items to materialize for large sequences within a query or for outputting on screen (used to be called --result-size). |
| --number-of-output-partitions | N/A | ad hoc | How many partitions to create in the output, i.e., the number of files that will be created in the output path directory.
| --log-path  |  log-path | file:///folder/log.txt  |  Where to output log information |
| --print-iterator-tree | N/A | yes, no | For debugging purposes, prints out the expression tree and runtime interator tree. |
| --show-error-info | show-error-info | yes, no | For debugging purposes. If you want to report a bug, you can use this to get the full exception stack. If no, then only a short message is shown in case of error. |
| --static-typing | static-typing | yes, no | Activates static type analysis, which annotates the expression tree with inferred types at compile time and enables more optimizations (experimental). Deactivated by default. |
| --server  | N/A  |  yes, no |  yes runs RumbleDB as a server on port 8001. Run queries with http://localhost:8001/jsoniq?query-path=/folder/foo.json |
| --port  | N/A  |  8001 (default) |  Changes the port of the RumbleDB HTTP server to any of your liking |
| --host  | N/A  |  localhost (default) |  Changes the host of the RumbleDB HTTP server to any of your liking |
| --variable:foo  | variable:foo  |  bar |  --variable:foo bar initialize the global variable $foo to "bar". The query must contain the corresponding global variable declaration, e.g., "declare variable $foo external;" |

