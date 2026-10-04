(:JIQS: ShouldRun; Output="(xs:float, true)" :)
let $result := max(
    parallelize((xs:float(1), xs:float(2)), 2) treat as xs:float+
) is statically xs:float
return (fn:item-type($result), $result eq xs:float(2))
