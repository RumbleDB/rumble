(:JIQS: ShouldRun; Output="(3, 1.5, 1, 2)" :)
let $values := (1, <a>2</a>)
return (
    sum($values) is statically xs:anyAtomicType,
    avg($values) is statically xs:anyAtomicType?,
    min($values) is statically xs:anyAtomicType?,
    max($values) is statically xs:anyAtomicType?
)
