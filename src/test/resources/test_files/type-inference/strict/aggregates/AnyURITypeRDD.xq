(:JIQS: ShouldRun; Output="(xs:anyURI, true)" :)
let $result := max(
    parallelize((xs:anyURI("z"), xs:anyURI("a")), 2) treat as xs:anyURI+
) is statically xs:anyURI
return (fn:item-type($result), $result eq xs:anyURI("z"))
