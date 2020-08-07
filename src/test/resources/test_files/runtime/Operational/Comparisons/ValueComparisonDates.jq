(:JIQS: ShouldRun; Output="(true, true, false, true, true, true)" :)
date("2001-12-12-12:00") eq date("2001-12-13+12:00"),
date("2001-12-12-12:00") ne date("2001-12-13Z"),
date("2001-12-12-10:00") le date("2001-12-12Z"),
date("2004-04-12Z") ge date("2004-04-12"),
date("2004-04-12Z") gt date("2004-04-12+02:00"),
date("2001-12-12-10:00") lt date(()),
date("2001-12-12-10:00") ge date(()),
date("2001-12-12-10:00") gt null
