(:JIQS: ShouldRun; Output="cardinality-error" :)
let $function := function() as xs:integer {
    exit returning (1, 2);
    0
}
return try {
    let $values := $function()
    return $values
} catch err:XPTY0004 {
    "cardinality-error"
}

(: A dynamic call must check the early result before a singleton variable reference can discard an item. :)
