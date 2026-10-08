(:JIQS: ShouldRun; Output="(true, true, true, 2, 2)" :)
let $name := "e"
return (
    (<e/> is statically element(e)) instance of element(e),
    (element e {} is statically element(e)) instance of element(e),
    (element {$name} {} is statically element()) instance of element(e),
    count((<e/>, <e/>) is statically element(e)+),
    count((<e/>, <other/>) is statically element()+)
)
