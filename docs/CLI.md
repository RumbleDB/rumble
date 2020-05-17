# Rumble parameters

The parameters that can be used on the command line as well as on the planned HTTP server are shown below.


|  Shell parameter | HTTP parameter  | example values  | Semantics  |
|---|---|---|---|
| --shell  | N/A  |  yes, no |  yes runs the interactive shell. No executes a query specified with --query-path |
| --query-path  | query-path  | file:///folder/file.jq  | A JSONiq query file to read from (from any file system).  |
| --output-path  |  output-path | file:///folder/output  | Where to output to (if the output is large, it will create a sharded directory, otherwise it will create a file) |
| --log-path  |  log-path | file:///folder/log.txt  |  Where to output log information |
| --result-size |  result-size | 2000 | A cap on the maximum number of items to materialize for large sequences within a query or for outputting on screen. |
| --overwrite  |  overwrite | yes, no | Whether to overwrite to --output-path. No throws an error if the output file/folder exists. |
| --print-iterator-tree | N/A | yes, no | For debugging purposes, prints out the expression tree and runtime interator tree. |
| --show-error-info | show-error-info | yes, no | For debugging purposes. If you want to report a bug, you can use this to get the full exception stack. If no, then only a short message is shown in case of error. |
| --server  | N/A  |  yes, no |  yes runs Rumble as a server on port 8001. Run queries with http://localhost:8001/jsoniq?query-path=/folder/foo.json |
| --port  | N/A  |  8001 (default) |  Changes the default port of the Rumble HTTP server to any of your liking |
| --variable:foo  | variable:foo  |  bar |  --variable:foo bar initialize the global variable $foo to "bar". The query must contain the corresponding global variable declaration, e.g., "declare variable $foo external;" |

