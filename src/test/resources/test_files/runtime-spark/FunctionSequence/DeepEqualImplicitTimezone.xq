(:JIQS: ShouldRun; Output="true":)
(: Deep equality compares atomic dateTimes using the implicit timezone.
   https://www.w3.org/TR/xpath-functions-31/#func-deep-equal :)
xquery version "3.1";

let $local := xs:dateTime("2026-11-26T12:00:00")
let $zoned := adjust-dateTime-to-timezone($local, implicit-timezone())
return deep-equal(parallelize($local, 2), parallelize($zoned, 2))
