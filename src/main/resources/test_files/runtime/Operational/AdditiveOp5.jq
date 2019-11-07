(:JIQS: ShouldRun; Output="(2006-08-12T13:20:00, 2008-03-12T18:21:10+02:00, 2014-06-27T04:06:05.512Z, 2004-08-15T04:56:22.500-14:00, P458DT2H9M47S, 2003-02-23T19:11:06.488-05:00, 1764-11-12T13:20:00-05:00)" :)
dateTime("2004-04-12T13:20:00") + yearMonthDuration("P2Y4M"),
yearMonthDuration("P3Y1M") + dateTime("2005-02-12T18:21:10+02:00"),
dateTime("2014-06-22T24:00:00Z") + dayTimeDuration("P4DT4H6M5.512S"),
dayTimeDuration("P124DT9H96M22.5S") + dateTime("2004-04-12T18:20:00-14:00"),
dateTime(()) + dayTimeDuration("P4DT4H6M5.512S"),
yearMonthDuration(()) + dateTime("2004-04-12T13:20:00"),
dateTime("2004-04-12T13:20:00") - dateTime("2003-01-11T01:10:13+14:00"),
dateTime("2004-04-12T13:20:00-05:00") - dayTimeDuration("P412DT41H68M53.512S"),
dateTime("2004-04-12T13:20:00-05:00") - yearMonthDuration("P233Y77M")

(: general tests :)
