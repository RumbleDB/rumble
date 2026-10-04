(:JIQS: ShouldRun; Output="(xs:decimal, true)" :)
let $result := max(
    parallelize((xs:decimal(1), xs:decimal(2)), 2) treat as xs:decimal+
) is statically xs:decimal
return (fn:item-type($result), $result eq xs:decimal(2))
