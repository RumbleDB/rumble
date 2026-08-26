(:JIQS: ShouldRun; Output="([Apr], [Mon], [April     ], [Monday], [April], [Monday])" :)
concat("[", format-date(date("2004-04-12"), "[MNn,*-3]"), "]"),
concat("[", format-date(date("2004-04-12"), "[FNn,*-3]"), "]"),
concat("[", format-date(date("2004-04-12"), "[MNn,10]"), "]"),
concat("[", format-date(date("2004-04-12"), "[FNn,3-*]"), "]"),
concat("[", format-date(date("2004-04-12"), "[MNn,*-10]"), "]"),
concat("[", format-date(date("2004-04-12"), "[FNn,*-6]"), "]")
