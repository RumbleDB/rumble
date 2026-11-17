(:JIQS: ShouldRun; Output="([12/4/2004 ], [], +05:00, [12/4/2004 +05:00])" :)
concat("[", format-date(date("2004-04-12"), "[D]/[M]/[Y] [Z]"), "]"),
concat("[", format-date(date("2004-04-12"), "[Z]"), "]"),
format-date(date("2004-04-12+05:00"), "[Z]"),
concat("[", format-date(date("2004-04-12+05:00"), "[D]/[M]/[Y] [Z]"), "]")
