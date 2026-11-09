(:JIQS: ShouldRun; Output="(xs:token, true, xs:token, true)" :)
(: With one primitive type, local and RDD max retain the selected derived type. :)
let $local := max((xs:token("a"), xs:token("b"))) is statically xs:string
let $distributed := max(
    parallelize((xs:token("a"), xs:token("b")), 2) treat as xs:token+
) is statically xs:string
return (fn:item-type($local), $local eq "b", fn:item-type($distributed), $distributed eq "b")
