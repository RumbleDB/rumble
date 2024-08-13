(:JIQS: ShouldRun; Output="({ "name" : false, "type" : false, "other" : true, "other2" : false, "anotherArray" : true }, { "name" : false, "type" : true, "other" : false, "other2" : false, "anotherArray" : false }, { "name" : false, "type" : false, "other" : true, "other2" : true, "anotherArray" : false })":)
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
return $data=>pandas:isnull()