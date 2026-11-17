(:JIQS: ShouldRun; Output="({ "label" : 1, "stringCol" : "foo bar foobar", "output" : [ "foo", "bar", "foobar" ] }, { "label" : 2, "stringCol" : "foo bar foobar", "output" : [ "foo", "bar", "foobar" ] }, { "label" : 3, "stringCol" : "foo bar foobar", "output" : [ "foo", "bar", "foobar" ] }, { "label" : 4, "stringCol" : "foo bar foobar", "output" : [ "foo", "bar", "foobar" ] }, { "label" : 5, "stringCol" : "foo bar foobar", "output" : [ "foo", "bar", "foobar" ] })" :)

let $data := annotate(
    for $i in 1 to 10000
    return { "label" : $i, "stringCol" : "foo bar foobar" },
    { "label": "integer", "stringCol": "string" }
)

let $transformer := get-transformer("Tokenizer")
for $result in $transformer(
    $data,
    {"inputCol": "stringCol", "outputCol": "output"}
)
count $c
where $c le 5
return {
    "label": $result.label,
    "stringCol": $result.stringCol,
    "output": $result.output
}
