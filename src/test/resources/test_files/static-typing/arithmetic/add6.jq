(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare variable $time := "13:20:00" cast as time;
declare variable $ymdur := "P1Y2M" cast as yearMonthDuration;
$time + $ymdur