(:JIQS: ShouldRun; Output="(xs:double, true)" :)
(: Even a NaN result uses the common numeric type of every input. :)
let $min := min(
    parallelize((xs:float("NaN"), xs:double(2)), 2) treat as xs:numeric+
)
return (fn:item-type($min), $min ne $min)
