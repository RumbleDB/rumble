(:JIQS: ShouldRun; Output="cardinality-error" :)
declare function local:values() as xs:integer {
    exit returning (1, 2);
    0
};

try {
    let $values := local:values()
    return $values
} catch err:XPTY0004 {
    "cardinality-error"
}

(: The final singleton expression must not suppress the return check on an early two-item result. :)
