(:JIQS: ShouldRun; Output="({ "name" : [ "null", "null" ], "type" : "null", "other" : [ 1, 2, 3 ], "other2" : 1, "anotherArray" : [ 1, 2, 3 ] }, { "name" : [ "null" ], "type" : [ 1, 2, 3 ], "other" : [ "null" ], "other2" : 2, "anotherArray" : [ [ 2, 1, 2 ] ] }, { "name" : [ "null" ], "type" : "null", "other" : [ 1, 2, 3 ], "other2" : [ 1, 2, 3 ], "anotherArray" : [ [ 23 ] ] })":)
import module namespace pandas = "jsoniq_pandas.jq";

declare type local:sample-type as {
    "name": ["string"],
    "type": "string",
    "other": ["string"],
    "other2": "integer",
    "anotherArray": [["integer"]]
};

declare variable $file_data := json-file("../../../queries/sample-na-data-3.json");
let $data := validate type local:sample-type* {$file_data}
return $data=>pandas:fillna({"value": [1, 2, 3]})

