(:JIQS: ShouldRun; Output="({ "id" : 1, "names" : { "a1" : [ "a", "b" ], "b1" : [ 11, 22 ] }, "nulls" : { "test" : [ null ] } }, { "id" : 2, "names" : { "a1" : [ "a", "b" ], "b1" : [ 11, 22 ] }, "nulls" : { "test" : [ null ] } })" :)
annotate(
    (
        {"id": 1, "names": {"a1": ["a", "b"], "b1": [11, 22]}, "nulls": {"test": [null]}},
        {"id": 2, "names": {"a1": ["a", "b"], "b1": [11, 22]}, "nulls": {"test": [null]}}
    ),
    {
        "id": "integer",
        "names": {"a1": ["string"], "b1": ["integer"]},
        "nulls": {"test": ["null"]}
    }
)

(: Nested Array in Object types in schema :)
