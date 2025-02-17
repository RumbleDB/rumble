(:JIQS: ShouldRun; Output="({ "id" : 1, "names" : [ "a", "b" ], "votes" : [ 15, 26 ], "nulls" : [ null ] }, { "id" : 2, "names" : [ "a", "b" ], "votes" : [ 36, 42 ], "nulls" : [ null, null ] }, { "id" : 3, "names" : [ "a", "b" ], "votes" : [ 26, 45 ], "nulls" : [ null, null, null ] })" :)
annotate(
    (
        {"id": 1, "names": ["a", "b"], "votes": [15, 26], "nulls": [null]},
        {"id": 2, "names": ["a", "b"], "votes": [36, 42], "nulls": [null, null]},
        {"id": 3, "names": ["a", "b"], "votes": [26, 45], "nulls": [null, null, null]}
    ),
    {
        "id": "integer",
        "names": ["string"],
        "votes": ["integer"],
        "nulls": ["null"]
    }
)

(: Array types in schema :)
