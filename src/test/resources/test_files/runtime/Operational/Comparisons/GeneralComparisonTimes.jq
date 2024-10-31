(:JIQS: ShouldRun; Output="(true, false, true, false, false, false, false)" :)
time("13:20:00") = time("13:20:00.000Z"),
time("13:20:30.5555") != time("13:20:30.555"),
time("13:20:00-05:00") >= time("13:20:00+02:00"),
time("13:20:00Z") < time("13:20:00"),
time("24:00:00") != time("00:00:00"),
time(()) > time("01:00:01"),
time(()) <= time("01:00:01")
