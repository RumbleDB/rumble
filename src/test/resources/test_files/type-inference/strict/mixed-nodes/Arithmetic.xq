(:JIQS: ShouldRun; Output="(4, 6, 1, -3, 5, 8, 2, -4)" :)
for $x in (3, <a>4</a>)
return (
    ($x + 1) is statically xs:anyAtomicType?,
    ($x * 2) is statically xs:anyAtomicType?,
    ($x idiv 2) is statically xs:integer?,
    (-$x) is statically xs:numeric?
)
