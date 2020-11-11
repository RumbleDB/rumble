(:JIQS: ShouldRun :)
declare $date := '2020-02-12' cast as date
declare $dt := "2015-05-03T13:20:00" cast as dateTime
declare $time := "13:20:00" cast as time
declare $dtdur := "P2DT3H" cast as dayTimeDuration
declare $ymdur := "P1Y2M" cast as yearMonthDuration
declare $hexb := "0cd7" cast as hexBinary
declare $base64b := "DNc=" cast as base64Binary
($date eq $date) is statically boolean, ($date lt $date) is statically boolean, ($dt eq $dt) is statically boolean, ($dt lt $dt) is statically boolean, ($time eq $time) is statically boolean, ($time gt $time) is statically boolean, ($ymdur eq $ymdur) is statically boolean, ($ymdur lt $ymdur) is statically boolean, ($dtdur eq $dtdur) is statically boolean, ($dtdur lt $dtdur) is statically boolean, ($ymdur eq $dtdur) is statically boolean, (($ymdur treat as duration) eq $dtdur) is statically boolean, ($hexb eq $hexb) is statically boolean, ($base64b eq $base64b) is statically boolean