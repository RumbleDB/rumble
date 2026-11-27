(:JIQS: ShouldRun; Output="true":)
(: A timezone-less dateTime is normalized using the implicit timezone before subtraction.
   Reproduce the failure with a non-UTC JVM timezone.
   https://www.w3.org/TR/xpath-functions-31/#func-subtract-dateTimes :)
let $local := xs:dateTime("2012-05-30T12:00:00")
let $with-timezone := adjust-dateTime-to-timezone($local, implicit-timezone())
return $local - $with-timezone eq xs:dayTimeDuration("PT0S")
