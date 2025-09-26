(:JIQS: ShouldRun; Output="({ "label" : 0, "object" : { "intarray" : [ 1 ], "stringarray" : [ "abc", "def" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 } ] }, { "label" : 1, "object" : { "intarray" : [ 2, 3 ], "stringarray" : [ "abc", "a" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 } ] }, { "label" : 2, "object" : { "intarray" : [ 4, 5, 6 ], "stringarray" : [ "abc" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 } ] }, { "label" : 3, "object" : { "intarray" : [ ], "stringarray" : [ "abc", "def" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 } ] }, { "label" : 4, "object" : { "intarray" : [ 4, 5, 6 ], "stringarray" : [ "abc" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 } ] }, { "label" : 5, "object" : { "intarray" : [ 4, 5, 6 ], "stringarray" : [ "abc" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 }, { "string" : "b", "decimal" : 5 } ] })" :)
annotate(
    json-lines("../../../../queries/rumbleML/sample-ml-data-nested3.json"),
    {
        "label": "integer",
        "object": {
            "intarray": ["integer"],
            "stringarray": ["string"]
        },
        "array": [{
            "string": "string",
            "decimal": "decimal"
        }]
    }
)

(: Array types in schema :)
