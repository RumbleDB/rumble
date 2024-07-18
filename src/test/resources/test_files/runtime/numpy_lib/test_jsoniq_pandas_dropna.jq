(:JIQS: ShouldRun; Output="({ "label" : 0, "binaryLabel" : 0, "name" : "a", "age" : 20, "weight" : 50, "booleanCol" : false, "stringCol" : "i am data entry 1", "stringArrayCol" : [ "i", "am", "data", "entry", "1" ], "intArrayCol" : [ 1, 2, 3 ], "doubleArrayCol" : [ 1, 2, 3 ], "doubleArrayArrayCol" : [ [ 1, 2, 3 ] ] }, { "label" : 1, "binaryLabel" : 0, "name" : "b", "age" : 21, "weight" : 55.3, "booleanCol" : false, "stringCol" : "i am data entry 2", "stringArrayCol" : [ "i", "am", "data", "entry", "2" ], "intArrayCol" : [ 4, 5, 6 ], "doubleArrayCol" : [ 4, 5, 6 ], "doubleArrayArrayCol" : [ [ 4, 5, 6 ] ] }, { "label" : 2, "binaryLabel" : 0, "name" : "c", "age" : 22, "weight" : 60.6, "booleanCol" : false, "stringCol" : "i am data entry 3", "stringArrayCol" : [ "i", "am", "data", "entry", "3" ], "intArrayCol" : [ 7, 8, 9 ], "doubleArrayCol" : [ 7, 8, 9 ], "doubleArrayArrayCol" : [ [ 7, 8, 9 ] ] }, { "label" : 3, "binaryLabel" : 1, "name" : "d", "age" : 23, "weight" : 65.9, "booleanCol" : false, "stringCol" : "i am data entry 4", "stringArrayCol" : [ "i", "am", "data", "entry", "4" ], "intArrayCol" : [ 1, 4, 7 ], "doubleArrayCol" : [ 1, 4, 7 ], "doubleArrayArrayCol" : [ [ 1, 4, 7 ] ] }, { "label" : 4, "binaryLabel" : 1, "name" : "e", "age" : 24, "weight" : 70.3, "booleanCol" : true, "stringCol" : "i am data entry 5", "stringArrayCol" : [ "i", "am", "data", "entry", "5" ], "intArrayCol" : [ 2, 5, 8 ], "doubleArrayCol" : [ 2, 5, 8 ], "doubleArrayArrayCol" : [ [ 2, 5, 8 ] ] }, { "label" : 5, "binaryLabel" : 1, "name" : "f", "age" : 25, "weight" : 75.6, "booleanCol" : true, "stringCol" : "i am data entry 6", "stringArrayCol" : [ "i", "am", "data", "entry", "6" ], "intArrayCol" : [ 3, 6, 9 ], "doubleArrayCol" : [ 3, 6, 9 ], "doubleArrayArrayCol" : [ [ 3, 6, 9 ] ] })":)
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