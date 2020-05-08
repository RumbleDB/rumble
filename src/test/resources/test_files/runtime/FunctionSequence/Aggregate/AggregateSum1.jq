(:JIQS: ShouldRun; Output="(6, 6, 6, 6, 6, 9, 306, 14, 0)" :)
sum((2, 4)),
sum((2, 4.0)),
sum((2, 4e+0)),
sum((2e+0, 4.0)),
sum((2.0, 4)),
sum((2, 4.0, 3e+0)),
sum((2, 4.0, 3e+2)),
sum((2, 3, (4, 5))),
sum(())

(: different numeric types :)
