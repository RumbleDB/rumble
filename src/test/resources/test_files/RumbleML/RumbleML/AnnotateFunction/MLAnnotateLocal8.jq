(:JIQS: ShouldRun; Output="({ "id" : 1, "names" : [ { "a" : "foo" }, { "a" : "bar" } ], "votes" : [ { "x" : 36 }, { "x" : 42 } ], "nulls" : [ { "test" : null } ] }, { "id" : 2, "names" : [ { "a" : "foo" }, { "a" : "bar" } ], "votes" : [ { "x" : 36 }, { "x" : 42 } ], "nulls" : [ { "test" : null } ] })" :)
annotate(
    (
        {"id": 1, "names": [{"a": "foo"}, {"a": "bar"}], "votes": [{"x": 36}, {"x": 42}], "nulls": [{"test": null}]},
        {"id": 2, "names": [{"a": "foo"}, {"a": "bar"}], "votes": [{"x": 36}, {"x": 42}], "nulls": [{"test": null}]}
    ),
    {
        "id": "integer",
        "names": [{"a": "string"}],
        "votes": [{"x": "integer"}],
        "nulls": [{"test": "null"}]
    }
)

(: Nested Object in Array types in schema :)
