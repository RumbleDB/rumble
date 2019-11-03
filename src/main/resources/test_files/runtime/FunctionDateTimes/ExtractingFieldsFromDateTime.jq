(:JIQS: ShouldRun; Output="(2004, 4, 12, 13, 20, 32.123, 0, PT2H)" :)
year-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
month-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
day-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
hours-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
minutes-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
seconds-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
seconds-from-dateTime(dateTime("2004-04-12T13:20:00+02:00")),
timezone-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
day-from-dateTime(())

(: general tests :)