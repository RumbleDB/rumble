(:JIQS: ShouldRun; Output="(12-4-2004, test4, no component test, a-0-b-20-c-13-d-12-e-4-f-2004, testMon\12test, 13, 04/12/2004 at 13:20:00)" :)
format-dateTime(dateTime("2004-04-12T13:20:00"), "[D]-[M]-[Y]"),
format-dateTime(dateTime("2004-04-12T13:20:15.5"), "test[M]"),
format-dateTime(dateTime("2004-04-12T13:20:00-05:00"), "no component test"),
format-dateTime(dateTime("2004-04-12T13:20:00Z"), "a-[s]-b-[m]-c-[H]-d-[D]-e-[M]-f-[Y]"),
format-dateTime(dateTime("2004-04-12T13:20:00+14:00"), "test[F]\\[D]test"),
format-dateTime(dateTime("2001-12-12T24:00:00"), "[D]"),
format-dateTime(dateTime(()), "test"),
format-dateTime(dateTime("2004-04-12T13:20:00"), "[M01]/[D01]/[Y0001] at [H01]:[m01]:[s01]")

(: general tests :)
