(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare variable $ymdur := "P1Y2M" cast as yearMonthDuration;
for $a in (1,2,3)
where $ymdur
return $a