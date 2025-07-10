# RumbleDB parameters

The parameters that can be used on the command line as well as on the planned HTTP server are shown below.

RumbleDB runs in three modes. You can select the mode passing a verb as the first parameter. For example:

```
   spark-submit rumbledb.org run file.jq -o output-dir -P 1
   spark-submit rumbledb.org run -q '1+1'
   spark-submit rumbledb.org serve -p 8001
   spark-submit rumbledb.org repl -c 10
```

Previous parameters (--shell, --query-path, --server) work in a backward compatible fashion, however we do recommend to start using the new verb-based format.

|  Shell parameter | Shortcut | HTTP parameter  | example values  | Semantics  |
|---|---|---|---|---|
| --shell  | repl | N/A  |  yes, no |  yes runs the interactive shell. No executes a query specified with --query-path |
| --shell-filter | N/A | N/A  |  jq . |  Post-processes the output of JSONiq queries on the shell with the specified command (reading the RumbleDB output via stdin) |
| --query | -q | query | 1+1  | A JSONiq query directly provided as a string.  |
| --query-path  | (any text without -- or - is recognized as a query path) | query-path  | file:///folder/file.jq  | A JSONiq query file to read from (from any file system, even the Web!).  |
| --output-path  | -o | output-path | file:///folder/output  | Where to output to (if the output is large, it will create a sharded directory, otherwise it will create a file) |
| --output-format  | -f | N/A | json, csv, avro, parquet, or any other format supported by Spark | An output format to use for the output. Formats other than json can only be output if the query outputs a highly structured sequence of objects (you can nest your query in an annotate() call to specify a schema if it does not). |
| --output-format-option:foo  | N/A | N/A | bar | Options to further specify the output format (example: separator character for CSV, compression format...) |
| --overwrite  | -O (meaning --overwrite yes) | overwrite | yes, no | Whether to overwrite to --output-path. No throws an error if the output file/folder exists. |
| --materialization-cap | -c |  materialization-cap | 200 | A cap on the maximum number of items to materialize for large sequences within a query or for outputting on screen (used to be called --result-size). |
| --number-of-output-partitions | -P | N/A | ad hoc | How many partitions to create in the output, i.e., the number of files that will be created in the output path directory.
| --log-path  | N/A | log-path | file:///folder/log.txt  |  Where to output log information |
| --print-iterator-tree | N/A | N/A | yes, no | For debugging purposes, prints out the expression tree and runtime interator tree. |
| --show-error-info | -v (meaning --show-error-info yes)  | show-error-info | yes, no | For debugging purposes. If you want to report a bug, you can use this to get the full exception stack. If no, then only a short message is shown in case of error. |
| --static-typing | -t (meaning --static-typing yes) | static-typing | yes, no | Activates static type analysis, which annotates the expression tree with inferred types at compile time and enables more optimizations (experimental). Deactivated by default. |
| --server  | serve | N/A  |  yes, no |  yes runs RumbleDB as a server on port 8001. Run queries with http://localhost:8001/jsoniq?query-path=/folder/foo.json |
| --port  | -p | N/A  |  8001 (default) |  Changes the port of the RumbleDB HTTP server to any of your liking |
| --host  | -h | N/A  |  localhost (default) |  Changes the host of the RumbleDB HTTP server to any of your liking |
| --variable:foo | N/A | variable:foo  |  bar |  --variable:foo bar initialize the global variable $foo to "bar". The query must contain the corresponding global variable declaration, e.g., "declare variable $foo external;" |
| --context-item | -I | context-item  |  bar |  initializes the global context item $$ to "bar". The query must contain the corresponding global variable declaration, e.g., "declare context item external;" |
| --context-item-input | -i | context-item-input  | - |  reads the context item value from the standard input |
| --context-item-input-format | N/A | context-item-input-format  |  text or json |  sets the input format to use for parsing the standard input (as text or as a serialized json value) |
| --dates-with-timezone | N/A | dates-with-timezone  |  yes or no | activates timezone support for the type xs:date (deactivated by default) |
| --optimize-general-comparison-to-value-comparison | N/A | optimize-general-comparison-to-value-comparison  |  yes or no | activates automatic conversion of general comparisons to value comparisons when applicable (activated by default) |
| --function-inlining | N/A | function-inlining  |  yes or no | activates function inlining for non-recursive functions (activated by default) |
| --parallel-execution | N/A | parallel-execution |  yes or no | activates parallel execution when possible (activated by default) |
| --native-execution | N/A | native-execution |  yes or no | activates native (Spark SQL) execution when possible (activated by default) |
| --default-language | N/A | N/A | jsoniq10, jsoniq31, xquery31 | specifies the query language to be used
| --optimize-steps | N/A | N/A | yes or no| allows RumbleDB to optimize steps, might violate stability of document order (activated by default)
| --optimize-steps-experimental | N/A | N/A | yes or no| experimentally optimizes steps more by skipping uniqueness and sorting in some cases. correctness is not yet verified (disabled by default)
| --optimize-parent-pointers | N/A | N/A | yes or no| allows RumbleDB to remove parent pointers from items if no steps requiring parent pointers are detected statically (activated by default)
| --static-base-uri | N/A | N/A | "../data/"| sets the static base uri for the execution. This option overwrites module location but is overwritten by declaration inside query