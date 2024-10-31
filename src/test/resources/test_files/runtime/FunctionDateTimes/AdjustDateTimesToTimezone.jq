(:JIQS: ShouldRun; Output="(13:20:00Z, 2014-03-12Z, 2004-04-12T13:20:15.500, 13:20:00, 2014-03-12+04:00, 2004-04-12T03:25:15+04:05, 04:20:00-14:00)" :)
adjust-time-to-timezone(time("13:20:00")),
fn:adjust-date-to-timezone(date("2014-03-12-05:00")),
adjust-dateTime-to-timezone(dateTime("2004-04-12T13:20:15.5"), ()),
adjust-time-to-timezone(time("13:20:00-05:00"), ()),
fn:adjust-time-to-timezone((), ()),
adjust-date-to-timezone(date("2014-03-12"), dayTimeDuration("PT4H")),
adjust-dateTime-to-timezone(dateTime("2004-04-12T13:20:15+14:00"), dayTimeDuration("PT4H5M")),
adjust-time-to-timezone(time("13:20:00-05:00"), dayTimeDuration("-PT14H")),
adjust-time-to-timezone((), dayTimeDuration("-PT14H"))
