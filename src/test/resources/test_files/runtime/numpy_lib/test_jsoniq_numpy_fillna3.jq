(:JIQS: ShouldRun; Output="({ "anotherArray" : [ 1, 2, 3 ], "name" : [ "null", "null" ], "other" : [ 1, 2, 3 ], "other2" : 1, "type" : "null" }, { "anotherArray" : [ [ 2, 1, 2 ] ], "name" : [ "null" ], "other" : [ "null" ], "other2" : 2, "type" : [ 1, 2, 3 ] }, { "anotherArray" : [ [ 23 ] ], "name" : [ "null" ], "other" : [ 1, 2, 3 ], "other2" : [ 1, 2, 3 ], "type" : "null" })":)
import module namespace pandas = "jsoniq_pandas.jq";

declare type local:sample-type as {
    "name": ["string"],
    "type": "string",
    "other": ["string"],
    "other2": "integer",
    "anotherArray": [["integer"]]
};

declare function local:order-by-keys($object as object*) as object* {
    for $row in $object
    return
        {|
            for $key in keys($row)
            order by $key
            return {$key: $row.$key}
        |}
};

declare variable $file_data := json-file("../../../queries/sample-na-data-3.json");
let $data := validate type local:sample-type* {$file_data}
return local:order-by-keys($data=>pandas:fillna({"value": [1, 2, 3]}))

