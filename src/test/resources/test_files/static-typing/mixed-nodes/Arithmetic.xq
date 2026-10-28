(:JIQS: ShouldRun; Output="(4, 6, 1, -3, 3, 5, 8, 2, -4, 4)" :)
(: The typed value of a parsed element is only known at runtime, unlike that of a constructed one. :)
declare function local:node($xml as xs:string) as element() { exactly-one(parse-xml($xml)/*) };
for $x in (3, local:node("<a>4</a>"))
return (
    ($x + 1) is statically xs:anyAtomicType?,
    ($x * 2) is statically xs:anyAtomicType?,
    ($x idiv 2) is statically xs:integer?,
    (-$x) is statically xs:numeric?,
    round($x) is statically xs:numeric?
)
