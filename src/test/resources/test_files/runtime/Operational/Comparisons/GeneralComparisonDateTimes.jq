(:JIQS: ShouldRun; Output="(true, true, true, false, false, true, false, false)" :)
dateTime("2001-12-12T12:00:00-12:00") = dateTime("2001-12-12T24:00:00"),
dateTime("2001-12-12T24:00:00") = dateTime("2001-12-13T00:00:00"),
dateTime("2001-12-12T12:00:00-10:00") = dateTime("2001-12-12T22:00:00"),
dateTime("2004-04-12T13:20:00Z") != dateTime("2004-04-12T13:20:00"),
dateTime("2004-04-12T13:20:00-02:00") < dateTime("2004-04-12T13:20:00"),
dateTime("2004-04-12T13:20:00-02:00") >= dateTime("2004-04-12T17:20:00+02:00"),
dateTime(()) = dateTime("2004-04-13T13:20:00"),
dateTime(()) != dateTime("2004-04-13T13:20:00")
