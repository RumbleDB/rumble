(:JIQS: ShouldRun; Output="3":)
(: Grouping keys retain fractional-second precision and use the implicit timezone.
   https://www.w3.org/TR/xquery-31/#id-group-by :)
xquery version "3.1";

let $first := xs:dateTime("2026-11-26T12:00:00.0001")
let $equivalent := adjust-dateTime-to-timezone($first, implicit-timezone())
let $second := xs:dateTime("2026-11-26T12:00:00.000100001+01:00")
let $third := xs:dateTime("2026-11-26T12:00:00.0002+01:00")
return count(
    for $key in parallelize(($first, $second, $third, $equivalent), 2)
    group by $key
    return $key
)
