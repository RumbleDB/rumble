(:JIQS: ShouldRun; Output="(xs:string, true)" :)
(: A generic atomic input cannot guarantee comparability under strict static typing. :)
let $result := max(
    parallelize((xs:anyURI("z"), "a"), 2) treat as xs:anyAtomicType+
)
return (fn:item-type($result), $result eq "z")
