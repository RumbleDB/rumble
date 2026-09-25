(:JIQS: ShouldRun; Output="(pm, am, pm, PM, [p], [pm    ])" :)
format-time(time("13:20:00"), "[P]"),
format-time(time("01:20:00"), "[P]"),
format-time(time("13:20:00"), "[Pn]"),
format-time(time("13:20:00"), "[PN]"),
concat("[", format-time(time("13:20:00"), "[P,*-1]"), "]"),
concat("[", format-time(time("13:20:00"), "[P,6]"), "]")
