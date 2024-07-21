(:JIQS: ShouldRun; Output="({ "categorical" : { "count" : 3, "unique" : 3, "top" : "f", "frequency" : 1 } }, { "numeric" : { "count" : 3, "mean" : 2, "std" : 1, "min" : 1, "max" : 3, "25%" : 1.5, "50%" : 2, "75%" : 2.5 } }, { "object" : { "count" : 3, "unique" : 3, "top" : "c", "frequency" : 1 } }, { "categorical" : null }, { "numeric" : { "count" : 3, "mean" : 2, "std" : 1, "min" : 1, "max" : 3, "25%" : 1.5, "50%" : 2, "75%" : 2.5 } }, { "object" : null }, { "categorical" : { "count" : 3, "unique" : 3, "top" : "f", "frequency" : 1 } }, { "numeric" : null }, { "object" : { "count" : 3, "unique" : 3, "top" : "c", "frequency" : 1 } }, { "0" : { "count" : 3, "mean" : 13, "std" : 4.358899, "min" : 10, "max" : 18, "25%" : 10.5, "50%" : 11, "75%" : 14.5 } }, { "1" : { "count" : 3, "mean" : 12, "std" : 3.6055512, "min" : 8, "max" : 15, "25%" : 10.5, "50%" : 13, "75%" : 14 } }, { "2" : { "count" : 3, "mean" : 10.6666666667, "std" : 8.621678, "min" : 3, "max" : 20, "25%" : 6, "50%" : 9, "75%" : 14.5 } }, { "Normal" : { "count" : 5, "mean" : 3, "std" : 1.5811388, "min" : 1, "max" : 5, "25%" : 2, "50%" : 3, "75%" : 4 } }, { "Uniform" : { "count" : 5, "mean" : 1, "std" : 0, "min" : 1, "max" : 1, "25%" : 1, "50%" : 1, "75%" : 1 } }, { "Skewed" : { "count" : 5, "mean" : 20.8, "std" : 44.274147, "min" : 1, "max" : 100, "25%" : 1, "50%" : 1, "75%" : 1 } })":)
import module namespace pandas = "jsoniq_pandas.jq";

declare type local:sample-type-with-arrays as {
    (: "label": "integer",
    "binaryLabel": "integer",
    "name": "string",
    "age": "integer",
    "weight": "double",
    "booleanCol": "boolean",
    "nullCol": "null",
    "stringCol": "string",
    "stringArrayCol": ["string"],
    "intArrayCol": ["integer"],
    "doubleArrayCol": ["double"], :)
    "doubleArrayArrayCol": [["double"]]
};

declare type local:sample-type-without-arrays as {
    "label": "integer",
    "binaryLabel": "integer",
    "name": "string",
    "age": "integer",
    "weight": "double",
    "booleanCol": "boolean",
    "nullCol": "null",
    "stringCol": "string"
};
(: pandas:describe({"categorical": ["d", "e", "f"],"numeric": [1, 2, 3],"object": ["a", "b", "c"]}),
pandas:describe({"categorical": ["d", "e", "f"],"numeric": [1, 2, 3],"object": ["a", "b", "c"]}, {"include": "number"}),
pandas:describe({"categorical": ["d", "e", "f"],"numeric": [1, 2, 3],"object": ["a", "b", "c"]}, {"include": "object"}),
pandas:describe({"0": [10, 18, 11],"1": [13, 15, 8], "2": [9, 20, 3]}),
pandas:describe({"Normal": [1, 2, 3, 4, 5],"Uniform": [1, 1, 1, 1, 1],"Skewed": [1, 1, 1, 1,100]}), :)
declare variable $file_data := json-file("../../../queries/sample-na-data-4.json");
let $data := validate type local:sample-type-with-arrays* {$file_data}
return $data=>pandas:describe()