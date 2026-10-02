(:JIQS: ShouldRun; Output="(true, true, true, true, true)" :)
(: XQuery's additional atomic types are available without an imported schema. :)
let $day := validate type xs:dayTimeDuration { <duration>PT2H</duration> }
let $month := validate type xs:yearMonthDuration { <duration>P2M</duration> }
return (
    <e a="value"/>/@a instance of attribute(*, xs:untypedAtomic),
    $day instance of element(*, xs:dayTimeDuration),
    $month instance of element(*, xs:yearMonthDuration),
    $day instance of element(*, xs:duration),
    $month instance of element(*, xs:duration)
)
