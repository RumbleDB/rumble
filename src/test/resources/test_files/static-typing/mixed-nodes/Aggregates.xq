(:JIQS: ShouldRun; Output="(3, 1.5, 1, 2, 7)" :)
(: The typed value of a parsed element is only known at runtime, unlike that of a constructed one. :)
declare function local:node($xml as xs:string) as element() { exactly-one(parse-xml($xml)/*) };
let $values := (1, local:node("<a>2</a>"))
return (
    sum($values) is statically xs:anyAtomicType,
    avg($values) is statically xs:anyAtomicType?,
    min($values) is statically xs:anyAtomicType?,
    max($values) is statically xs:anyAtomicType?,
    sum((), local:node("<zero>7</zero>")) is statically xs:anyAtomicType?
)
