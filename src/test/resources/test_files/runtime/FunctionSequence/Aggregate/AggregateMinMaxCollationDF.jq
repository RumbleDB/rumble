(:JIQS: ShouldRun; Output="true" :)
let $values := annotate(
    for $value in ("a", "B") return {"value": $value},
    {"value": "string"}
).value,
    $collation := "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive"
return max($values, $collation) eq "B" and min($values, $collation) eq "a"
