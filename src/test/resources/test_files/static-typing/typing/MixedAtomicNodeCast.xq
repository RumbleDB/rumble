(:JIQS: ShouldRun; Output="(3, 3, 3, true, 4, 4, 4, true)" :)
for $x in ('3', <a>4</a>)
return (
    ($x cast as xs:integer) is statically xs:integer,
    ($x cast as xs:integer?) is statically xs:integer?,
    xs:integer($x) is statically xs:integer?,
    ($x castable as xs:integer) is statically xs:boolean
)
