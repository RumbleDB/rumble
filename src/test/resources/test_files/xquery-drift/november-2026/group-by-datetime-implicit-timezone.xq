(:JIQS: ShouldRun; Output="true":)
(: Equivalent dateTime grouping keys form one group when compared using the implicit timezone.
   Reproduce the failure with a non-UTC JVM timezone.
   https://www.w3.org/TR/xquery-31/#id-group-by :)
let $local := xs:dateTime("2012-05-30T12:00:00")
let $with-timezone := adjust-dateTime-to-timezone($local, implicit-timezone())
return count(
    for $key in ($local, $with-timezone)
    group by $key
    return $key
) eq 1
