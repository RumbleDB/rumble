(:JIQS: ShouldRun; Output="(false, false)" :)
for $x in (xs:dayTimeDuration("P1D"), xs:yearMonthDuration("P1M"))
return ($x lt $x) is statically xs:boolean
