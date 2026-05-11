(:JIQS: ShouldRun; Output="({ "id" : 1, "names" : { "a" : "foo", "b" : "bar" }, "votes" : { "x" : 36, "y" : 42 }, "nulls" : { "test" : null } }, { "id" : 2, "names" : { "a" : "bar", "b" : "foo" }, "votes" : { "x" : 26, "y" : 45 }, "nulls" : { "test" : null } })" :)
annotate(
    (
        {"id": 1, "names": {"a": "foo", "b": "bar"}, "votes": {"x": 36, "y": 42}, "nulls": {"test": null}},
        {"id": 2, "names": {"a": "bar", "b": "foo"}, "votes": {"x": 26, "y": 45}, "nulls": {"test": null}}
    ),
    {
        "id": "integer",
        "names": {"a": "string", "b": "string"},
        "votes": {"x": "integer", "y": "integer"},
        "nulls": {"test": "null"}
    }
)

(: Object types in schema :)
