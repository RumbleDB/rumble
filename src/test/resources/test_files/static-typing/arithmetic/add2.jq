(:JIQS: ShouldRun :)
declare variable $date := "2020-02-12" cast as date;
declare variable $dt := "2015-05-03T13:20:00" cast as dateTime;
declare variable $time := "13:20:00" cast as time;
declare variable $dtdur := "P2DT3H" cast as dayTimeDuration;
declare variable $ymdur := "P1Y2M" cast as yearMonthDuration;
($dt + $dtdur) is statically dateTime, ($dt + $ymdur) is statically dateTime, ($date + $dtdur) is statically date, ($date + $ymdur) is statically date, ($time + $dtdur) is statically time, ($dtdur + $dtdur) is statically dayTimeDuration, ($ymdur + $ymdur) is statically yearMonthDuration, ($dt - $dt) is statically dayTimeDuration, ($date - $date) is statically dayTimeDuration, ($time - $time) is statically dayTimeDuration