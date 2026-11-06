(:JIQS: ShouldRun; Output="(true, false, true, false, false, false, false, false)" :)
(: null is comparable with every atomic value, for equality and ordering, on either side. :)
for $x in (1, null)
return (
    ($x eq 1) is statically xs:boolean,
    (1 lt $x) is statically xs:boolean,
    ($x = 1) is statically xs:boolean,
    (1 < $x) is statically xs:boolean
)
