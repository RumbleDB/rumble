(:JIQS: ShouldRun; Output="({ "id" : 1, "names" : { "a1" : { "a2" : "foo" }, "b1" : { "b2" : "bar" } }, "votes" : { "x1" : { "x2" : 36 }, "y1" : { "y2" : 42 } }, "nulls" : { "test1" : { "test2" : null } } }, { "id" : 2, "names" : { "a1" : { "a2" : "foo" }, "b1" : { "b2" : "bar" } }, "votes" : { "x1" : { "x2" : 36 }, "y1" : { "y2" : 42 } }, "nulls" : { "test1" : { "test2" : null } } })" :)
annotate(
    (
        {"id": 1, "names": {"a1": {"a2": "foo"}, "b1": {"b2": "bar"}}, "votes": {"x1": {"x2": 36}, "y1": {"y2": 42}}, "nulls": {"test1": {"test2": null}}},
        {"id": 2, "names": {"a1": {"a2": "foo"}, "b1": {"b2": "bar"}}, "votes": {"x1": {"x2": 36}, "y1": {"y2": 42}}, "nulls": {"test1": {"test2": null}}}
    ),
    {
        "id": "integer",
        "names": {"a1": {"a2": "string"}, "b1": {"b2": "string"}},
        "votes": {"x1": {"x2": "integer"}, "y1": {"y2": "integer"}},
        "nulls": {"test1": {"test2": "null"}}
    }
)

(: Nested Object types in schema :)
