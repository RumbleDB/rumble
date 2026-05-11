(:JIQS: ShouldRun; Output="(12-4-2004, test4, no component test, testMon\12test, 1, test12004-a-b-12-test4test, 2004-04-12, 12 Apr 2004, April 12, 2004, 12 April, 2004, Monday 12 April 2004, [2004-04-12])" :)
format-date(date("2004-04-12"), "[D]-[M]-[Y]"),
format-date(date("2004-04-12-05:00"), "test[M]"),
format-date(date("2004-04-12Z"), "no component test"),
format-date(date("2004-04-12+14:00"), "test[F]\\[D]test"),
format-date(date("-0045-01-01"), "[D]"),
format-date(date("12004-04-12"), "test[Y]-a-b-[D]-test[M]test"),
format-date(date(()), "test"),
format-date(date("2004-04-12"), "[Y0001]-[M01]-[D01]"),
format-date(date("2004-04-12"), "[D01] [MNn,*-3] [Y0001]"),
format-date(date("2004-04-12"), "[MNn] [D], [Y]"),
format-date(date("2004-04-12"), "[D] [MNn], [Y]"),
format-date(date("2004-04-12"), "[FNn] [D] [MNn] [Y]"),
format-date(date("2004-04-12"), "[[[Y0001]-[M01]-[D01]]]")

(: general tests :)
