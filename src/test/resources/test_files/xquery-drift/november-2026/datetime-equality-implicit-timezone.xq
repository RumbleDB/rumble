(:JIQS: ShouldRun; Output="true":)
(: A timezone-less dateTime is compared using the implicit timezone.
   Reproduce the failure with a non-UTC JVM timezone.
   https://www.w3.org/TR/xpath-functions-31/#func-dateTime-equal :)
let $local := xs:dateTime("2012-05-30T12:00:00")
let $with-timezone := adjust-dateTime-to-timezone($local, implicit-timezone())
return $local eq $with-timezone
