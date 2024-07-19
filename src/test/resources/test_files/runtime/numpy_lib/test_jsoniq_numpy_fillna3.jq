(:JIQS: ShouldRun; Output="({ "name" : [ null ], "type" : "null", "other2" : { "x" : null }, "other" : [ 1, 2, 3 ], "anotherArray" : [ 1, 2, 3 ] }, { "name" : [ null ], "type" : "null", "other2" : { "x" : "3" }, "other" : [ "null" ], "anotherArray" : [ [ 2, 1, 2 ] ] }, { "name" : [ null ], "type" : "null", "other2" : { }, "other" : [ 1, 2, 3 ], "anotherArray" : [ [ 23 ] ] })":)
import module namespace pandas = "jsoniq_pandas.jq";

declare type local:sample-type as {
    "name": ["null"],
    "type": "string",
    "other": ["string"],
    "other2": "object",
    "anotherArray": [["integer"]]
};

declare variable $file_data := json-file("../../../queries/sample-na-data-3.json");
let $data := validate type local:sample-type* {$file_data}
return $data=>pandas:fillna({"value": [1,2,3]})