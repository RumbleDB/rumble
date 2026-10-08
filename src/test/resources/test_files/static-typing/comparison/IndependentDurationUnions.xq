(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: Equal union types do not make independently bound values comparable. :)
for $x in (xs:dayTimeDuration("P1D"), xs:yearMonthDuration("P1M"))
for $y in (xs:dayTimeDuration("P1D"), xs:yearMonthDuration("P1M"))
return $x lt $y
