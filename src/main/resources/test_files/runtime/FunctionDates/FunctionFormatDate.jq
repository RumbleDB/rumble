(:JIQS: ShouldRun; Output="(12-4-2004, test4, no component test, test1\12test, 1, test12004-a-b-12-test4test)" :)
format-date(date("2004-04-12"), "[D]-[M]-[Y]"),
format-date(date("2004-04-12-05:00"), "test[M]"),
format-date(date("2004-04-12Z"), "no component test"),
format-date(date("2004-04-12+14:00"), "test[F]\\[D]test"),
format-date(date("-0045-01-01"), "[D]"),
format-date(date("12004-04-12"), "test[Y]-a-b-[D]-test[M]test"),
format-date(date(()), "test")

(: general tests :)