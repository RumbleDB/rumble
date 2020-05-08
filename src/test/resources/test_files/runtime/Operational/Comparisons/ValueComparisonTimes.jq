(:JIQS: ShouldRun; Output="(true, false, true, false, false)" :)
time("13:20:00") eq time("13:20:00.000Z"),
time("13:20:30.5555") ne time("13:20:30.555"),
time("13:20:00-05:00") ge time("13:20:00+02:00"),
time("13:20:00Z") lt time("13:20:00"),
time("24:00:00") ne time("00:00:00"),
time(()) gt time("01:00:01"),
time(()) le time("01:00:01")
