(:JIQS: ShouldRun; Output="(true, true, true, 2, 2)" :)
let $name := "e"
return (
    (<e/> is statically element(e, xs:untyped)) instance of element(e),
    (element e {} is statically element(e, xs:untyped)) instance of element(e),
    (element {$name} {} is statically element(*, xs:untyped)) instance of element(e),
    count((<e/>, <e/>) is statically element(e, xs:untyped)+),
    count((<e/>, <other/>) is statically element()+)
)
