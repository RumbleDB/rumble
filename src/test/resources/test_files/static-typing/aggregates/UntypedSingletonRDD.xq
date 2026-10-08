(:JIQS: ShouldRun; Output="(xs:double, true, xs:double, true, xs:double, true)" :)
(: A singleton reduction still needs the untyped-atomic conversion. :)
let $sum := sum(parallelize(xs:untypedAtomic("1"), 2) treat as xs:untypedAtomic+) is statically xs:double
let $min := min(parallelize(xs:untypedAtomic("1"), 2) treat as xs:untypedAtomic+) is statically xs:double
let $max := max(parallelize(xs:untypedAtomic("1"), 2) treat as xs:untypedAtomic+) is statically xs:double
return (
    fn:item-type($sum), $sum eq xs:double(1),
    fn:item-type($min), $min eq xs:double(1),
    fn:item-type($max), $max eq xs:double(1)
)
