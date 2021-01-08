(:JIQS: ShouldRun; Output="(0, 500.5, P1Y, P1Y, 500.5, 0)" :)
avg(parallelize(())),
avg(parallelize(1 to 1000)),
avg(parallelize(1 to 1000) ! yearMonthDuration("P1Y"), yearMonthDuration("P0Y")),
avg((1 to 1000) ! yearMonthDuration("P1Y"), yearMonthDuration("P0Y")),
avg(annotate(parallelize(1 to 10000) ! { "foo" : $$ cast as double }, { "foo" : "double" }).foo),
avg(annotate(parallelize(1 to 10000) ! { "foo" : $$ cast as double }, { "foo" : "double" }).bar)
