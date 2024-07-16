(:JIQS: ShouldRun; Output="({ "label" : 4, "binaryLabel" : 1, "name" : "e", "age" : 24, "weight" : 70.3, "booleanCol" : true, "nullCol" : null, "stringCol" : "i am data entry 5", "stringArrayCol" : [ "i", "am", "data", "entry", "5" ], "intArrayCol" : [ 2, 5, 8 ], "doubleArrayCol" : [ 2, 5, 8 ], "doubleArrayArrayCol" : [ [ 2, 5, 8 ] ] }, { "label" : 0, "binaryLabel" : 0, "name" : "a", "age" : 20, "weight" : 50, "booleanCol" : false, "nullCol" : null, "stringCol" : "i am data entry 1", "stringArrayCol" : [ "i", "am", "data", "entry", "1" ], "intArrayCol" : [ 1, 2, 3 ], "doubleArrayCol" : [ 1, 2, 3 ], "doubleArrayArrayCol" : [ [ 1, 2, 3 ] ] }, { "label" : 0, "binaryLabel" : 0, "name" : "a", "age" : 20, "weight" : 50, "booleanCol" : false, "nullCol" : null, "stringCol" : "i am data entry 1", "stringArrayCol" : [ "i", "am", "data", "entry", "1" ], "intArrayCol" : [ 1, 2, 3 ], "doubleArrayCol" : [ 1, 2, 3 ], "doubleArrayArrayCol" : [ [ 1, 2, 3 ] ] })":)
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
return $data=>pandas:sample(3, 3)