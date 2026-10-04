(:JIQS: ShouldRun; Output="(xs:string, true)" :)
(: Mixing primitive string and URI types also converts a winning xs:token to xs:string. :)
let $result := max(
    parallelize((xs:token("z"), xs:anyURI("a")), 2) treat as xs:anyAtomicType+
)
return (fn:item-type($result), $result eq "z")
