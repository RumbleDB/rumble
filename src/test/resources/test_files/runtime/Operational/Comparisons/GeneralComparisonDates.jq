(:JIQS: ShouldRun; Output="(true, true, false, true, true, false, false, true)" :)
date("2001-12-12-12:00") = date("2001-12-13+12:00"),
date("2001-12-12-12:00") != date("2001-12-13Z"),
date("2001-12-12-10:00") <= date("2001-12-12Z"),
date("2004-04-12Z") >= date("2004-04-12"),
date("2004-04-12Z") > date("2004-04-12+02:00"),
date("2001-12-12-10:00") < date(()),
date("2001-12-12-10:00") >= date(()),
date("2001-12-12-10:00") > null
