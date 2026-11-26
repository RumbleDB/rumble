(:JIQS: ShouldRun; Output="true":)
(: The one-argument function uses the dynamic context's implicit timezone.
   Reproduce the failure with a non-UTC JVM timezone.
   https://www.w3.org/TR/xpath-functions-31/#func-adjust-time-to-timezone :)
let $time := xs:time("12:00:00")
return timezone-from-time(adjust-time-to-timezone($time)) eq implicit-timezone()
