(:JIQS: ShouldRun; Output="(2001-12-13T00:00:00, 2004-04-12T13:20:00+14:00, 2004-04-12T13:20:00, 2004-04-12T13:20:00Z, 2004-04-12T13:20:15.500, 2004-04-12T13:20:00-05:00)" :)
for $j as dateTime in parallelize((dateTime("2004-04-12T13:20:00"), dateTime("2004-04-12T13:20:15.5"), dateTime("2004-04-12T13:20:00-05:00"), dateTime("2004-04-12T13:20:00Z"), dateTime("2004-04-12T13:20:00+14:00"), dateTime("2001-12-12T24:00:00"), dateTime(())))
order by $j
return $j