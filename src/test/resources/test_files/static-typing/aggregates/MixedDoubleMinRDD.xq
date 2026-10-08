(:JIQS: ShouldRun; Output="(xs:double, true)" :)
let $result := min(
    parallelize((xs:float(1), xs:double(2)), 2) treat as xs:numeric+
)
return (fn:item-type($result), $result eq xs:double(1))
