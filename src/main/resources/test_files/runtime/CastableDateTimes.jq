(:JIQS: ShouldRun; Output="(true, true, false, true, true, true, false, true, true, false, false)" :)
dateTime("2001-12-12T23:00:00") castable as dateTime,
dateTime("2001-12-12T23:00:00Z") castable as string,
dateTime("2001-12-12T23:00:00-02:00") castable as duration,
"2004-04-13T00:00:00.000+03:00" castable as dateTime,
date("2001-12-12-10:00") castable as string,
date("2001-12-12-10:00") castable as dateTime,
date("2001-12-12-10:00") castable as duration,
dateTime("2001-12-12T23:00:00-02:00") castable as date,
"3011-02-11-02:00" castable as date,
"2001-12-12T23:00:00" castable as date,
"2001-12-32" castable as date