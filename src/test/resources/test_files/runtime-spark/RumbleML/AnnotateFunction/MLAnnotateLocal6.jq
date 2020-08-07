(:JIQS: ShouldRun; Output="({ "id" : 1, "names" : [ [ "a", "b" ], [ "c", "d" ] ], "votes" : [ [ 11, 22 ], [ 33, 44 ] ], "nulls" : [ [ null, null ], [ null, null ] ] }, { "id" : 2, "names" : [ [ "e", "f" ], [ "g", "h" ] ], "votes" : [ [ 55, 66 ], [ 77, 88 ] ], "nulls" : [ [ null, null ], [ null, null ] ] })" :)
annotate(
    (
        {"id": 1, "names": [["a", "b"], ["c", "d"]], "votes": [[11, 22], [33, 44]], "nulls": [[null, null], [null, null]]},
        {"id": 2, "names": [["e", "f"], ["g", "h"]], "votes": [[55, 66], [77, 88]], "nulls": [[null, null], [null, null]]}
    ),
    {
        "id": "integer",
        "names": [["string"]],
        "votes": [["integer"]],
        "nulls": [["null"]]
    }
)

(: Nested Array types in schema :)
