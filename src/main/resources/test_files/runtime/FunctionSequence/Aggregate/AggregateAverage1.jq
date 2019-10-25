(:JIQS: ShouldRun; Output="(3, 3.5, 3, 3, 3, 3, 3, 102, 3.5)" :)
avg((2, 4)),
avg((2, 5)),
avg((2, 4.0)),
avg((2, 4e+0)),
avg((2e+0, 4.0)),
avg((2.0, 4)),
avg((2, 4.0, 3e+0)),
avg((2, 4.0, 3e+2)),
avg((2, 3, (4, 5))),
avg(())

(: different numeric types :)
