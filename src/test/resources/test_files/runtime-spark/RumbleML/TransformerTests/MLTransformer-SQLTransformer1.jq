(:JIQS: ShouldRun; Output="({ "id" : 0, "v1" : 1, "v2" : 3, "v3" : 4, "v4" : 3 }, { "id" : 2, "v1" : 2, "v2" : 5, "v3" : 7, "v4" : 10 })" :)
let $raw-data := parallelize((
    {"id": 0, "v1": 1.0, "v2": 3.0},
    {"id": 2, "v1": 2.0, "v2": 5.0}
))

let $data := annotate(
    $raw-data
    { "id": "integer", "v1": "double", "v2": "double" }
)

let $transformer := get-transformer("SQLTransformer")
for $result in $transformer(
    $data,
    { "statement": "SELECT *, (v1 + v2) AS v3, (v1 * v2) AS v4 FROM __THIS__"}
)
return {
    "id": $result.id,
    "v1": $result.v1,
    "v2": $result.v2,
    "v3": $result.v3,
    "v4": $result.v4
}
