(:JIQS: ShouldRun; Output="(xs:float, true)" :)
let $result := max(
    parallelize((1, xs:float(0)), 2) treat as xs:numeric+
)
return (fn:item-type($result), $result eq xs:float(1))
