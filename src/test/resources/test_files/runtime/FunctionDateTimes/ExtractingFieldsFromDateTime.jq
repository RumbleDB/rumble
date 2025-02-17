(:JIQS: ShouldRun; Output="(2004, 4, 12, 13, 20, 32.123, 0, PT2H, 2004, 4, 12, -PT14H, 13, 20, 32.123, 0, PT0S)" :)
year-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
month-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
day-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
hours-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
minutes-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
seconds-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
seconds-from-dateTime(dateTime("2004-04-12T13:20:00+02:00")),
timezone-from-dateTime(dateTime("2004-04-12T13:20:32.123+02:00")),
year-from-date(date("2004-04-12")),
month-from-date(date("2004-04-12+02:00")),
day-from-date(date("2004-04-12Z")),
timezone-from-date(date("2004-04-12-14:00")),
hours-from-time(time("13:20:32.123+02:00")),
minutes-from-time(time("13:20:32.123+02:00")),
seconds-from-time(time("13:20:32.123+02:00")),
seconds-from-time(time("13:20:00+02:00")),
timezone-from-time(time("13:20:00Z")),
day-from-dateTime(())

(: general tests :)
