(:JIQS: ShouldRun; Output="(xs:token, true)" :)
(: Promoting an anyURI candidate to string preserves a winning string subtype. :)
let $result := max(
    parallelize((xs:token("z"), xs:anyURI("a")), 2) treat as xs:anyAtomicType+
)
return (fn:item-type($result), $result eq "z")
