(:JIQS: ShouldRun; Output="({ "label" : { "count" : 6, "mean" : 2.5, "std" : 1.8708287, "min" : 0, "max" : 5, "25%" : 1.25, "50%" : 2.5, "75%" : 3.75 } }, { "binaryLabel" : { "count" : 6, "mean" : 0.5, "std" : 0.5477226, "min" : 0, "max" : 1, "25%" : 0, "50%" : 0.5, "75%" : 1 } }, { "name" : { "count" : 6, "unique" : 6, "top" : "c", "frequency" : 1 } }, { "age" : { "count" : 6, "mean" : 22.5, "std" : 1.8708287, "min" : 20, "max" : 25, "25%" : 21.25, "50%" : 22.5, "75%" : 23.75 } }, { "weight" : { "count" : 6, "mean" : 62.95000000000001, "std" : 9.534097, "min" : 50, "max" : 75.6, "25%" : 56.625, "50%" : 63.25, "75%" : 69.2 } }, { "booleanCol" : { "count" : 6, "unique" : 3, "top" : false, "frequency" : 3 } }, { "nullCol" : { "count" : 6, "unique" : 1, "top" : null, "frequency" : 6 } }, { "stringCol" : { "count" : 6, "unique" : 6, "top" : "i am data entry 6", "frequency" : 1 } }, { "stringArrayCol" : { "count" : 6, "unique" : 5, "top" : [ "i", "am", "data", "entry", "3" ], "frequency" : 2 } }, { "intArrayCol" : { "count" : 6, "unique" : 6, "top" : [ 1, 2, 3 ], "frequency" : 1 } }, { "doubleArrayCol" : { "count" : 6, "unique" : 6, "top" : [ 1, 2, 3 ], "frequency" : 1 } }, { "doubleArrayArrayCol" : { "count" : 6, "unique" : 5, "top" : [ [ 1, 2, 3 ] ], "frequency" : 2 } })":)
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

declare variable $file_data := json-file("../../../queries/sample-na-data-4.json");
let $data := validate type local:sample-type-with-arrays* {$file_data}
return $data=>pandas:describe()