(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare variable $duration := xs:duration("P1D");
for $a in ($duration, $duration)
order by $a
return $a
