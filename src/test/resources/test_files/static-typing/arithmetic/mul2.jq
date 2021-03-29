(:JIQS: ShouldRun :)
declare variable $dtdur := "P2DT3H" cast as dayTimeDuration;
declare variable $ymdur := "P1Y2M" cast as yearMonthDuration;
(3 * $ymdur) is statically yearMonthDuration, (3e4 * $ymdur) is statically yearMonthDuration, (2 * $dtdur) is statically dayTimeDuration, ($dtdur * 3.12) is statically dayTimeDuration, ($ymdur div 4.3) is statically yearMonthDuration, ($dtdur div 12) is statically dayTimeDuration, ($ymdur div $ymdur) is statically decimal, ($dtdur div $dtdur) is statically decimal