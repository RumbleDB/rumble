(:JIQS: ShouldRun; Output="":)
import module namespace pandas = "jsoniq_pandas.jq";

declare type local:sample-type-with-arrays as {
    "label": "integer",
    "binaryLabel": "integer",
    "name": "string",
    "age": "integer",
    "weight": "double",
    "booleanCol": "boolean",
    "nullCol": "null",
    "stringCol": "string",
    "stringArrayCol": ["string"],
    "intArrayCol": ["integer"],
    "doubleArrayCol": ["double"],
    "doubleArrayArrayCol": [["double"]]
};

declare variable $file_data := json-file("../../../queries/sample-na-data.json");
let $data := validate type local:sample-type-with-arrays* {$file_data}
return $data=>pandas:dropna({"axis": 1, "how": "any"})