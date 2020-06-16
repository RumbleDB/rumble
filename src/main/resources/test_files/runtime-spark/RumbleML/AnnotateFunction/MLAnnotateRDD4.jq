(:JIQS: ShouldRun; Output="({ "label" : 0, "object" : { "intarray" : [ 1, 2, 3 ], "stringarray" : [ "abc" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 } ] }, { "label" : 1, "object" : { "intarray" : [ 4, 5, 6 ], "stringarray" : [ "abc" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 } ] }, { "label" : 2, "object" : { "intarray" : [ 7, 8, 9 ], "stringarray" : [ "abc" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 } ] }, { "label" : 3, "object" : { "intarray" : [ 1, 4, 7 ], "stringarray" : [ "abc" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 } ] }, { "label" : 4, "object" : { "intarray" : [ 2, 5, 8 ], "stringarray" : [ "abc" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 } ] }, { "label" : 5, "object" : { "intarray" : [ 3, 6, 9 ], "stringarray" : [ "abc" ] }, "array" : [ { "string" : "a", "decimal" : 3.5 } ] })" :)
annotate(
    json-file("../../../../queries/rumbleML/sample-ml-data-nested2.json"),
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
