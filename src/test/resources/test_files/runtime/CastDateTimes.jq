(:JIQS: ShouldRun; Output="(2001-12-12T23:00:00, 2001-12-12T23:00:00Z, 2001-12-12T23:00:00-02:00, 2004-04-13T00:00:00, 2001-12-12-10:00, 2001-12-12T00:00:00-10:00, 2001-12-12-02:00, 3011-02-11-02:00, 13:20:20+09:00, 23:12:00.123, 12:34:56.789Z)" :)
dateTime("2001-12-12T23:00:00") cast as dateTime,
dateTime("2001-12-12T23:00:00Z") cast as dateTime,
dateTime(" 2001-12-12T23:00:00-02:00") cast as dateTime,
"2004-04-12T24:00:00  " cast as dateTime,
date("2001-12-12-10:00") cast as string,
date("2001-12-12-10:00") cast as dateTime,
dateTime("2001-12-12T23:00:00-02:00") cast as date,
"3011-02-11-02:00" cast as date,
time("13:20:20+09:00") cast as time,
time("23:12:00.123") cast as string,
dateTime("2001-12-12T12:34:56.789Z") cast as time
