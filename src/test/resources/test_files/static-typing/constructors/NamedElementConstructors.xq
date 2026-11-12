(:JIQS: ShouldRun; Output="(true, true, true, 2, 2)" :)
(: XQuery constructs elements in construction mode preserve by default, so they are annotated xs:anyType. :)
let $name := "e"
return (
    (<e/> is statically element(e, xs:anyType)) instance of element(e),
    (element e {} is statically element(e, xs:anyType)) instance of element(e),
    (element {$name} {} is statically element(*, xs:anyType)) instance of element(e),
    count((<e/>, <e/>) is statically element(e, xs:anyType)+),
    count((<e/>, <other/>) is statically element()+)
)
