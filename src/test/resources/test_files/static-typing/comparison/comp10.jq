(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare variable $dtdur := "P2DT3H" cast as dayTimeDuration;
declare variable $ymdur := "P1Y2M" cast as yearMonthDuration;
$ymdur lt $dtdur