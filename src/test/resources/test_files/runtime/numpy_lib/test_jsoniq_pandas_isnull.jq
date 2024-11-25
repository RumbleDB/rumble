(:JIQS: ShouldRun; Output="({ "anotherArray" : true, "name" : false, "other" : true, "other2" : false, "type" : false }, { "anotherArray" : false, "name" : false, "other" : false, "other2" : false, "type" : true }, { "anotherArray" : false, "name" : false, "other" : true, "other2" : true, "type" : false })":)
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

declare variable $file_data := json-lines("../../../queries/sample-na-data-3.json");
let $data := validate type local:sample-type* {$file_data}
return local:order-by-keys($data=>pandas:isnull())