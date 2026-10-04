(:JIQS: ShouldRun; Output="(xs:double, true)" :)
let $result := sum(
    parallelize((xs:untypedAtomic("1"), xs:untypedAtomic("2")), 2)
    treat as xs:untypedAtomic+
) is statically xs:double
return (fn:item-type($result), $result eq xs:double(3))
