(:JIQS: ShouldRun; Output="(3, 2, -2, 1.13, 8500, 3.14, NaN, INF, -INF, 0, -0)" :)
fn:round(2.5),
round(2.4999),
round(-2.5),
round(1.125, 2),
round(8452, -2),
round(3.1415e0, 2),
round((), 2),
round((), ()),
round(double("NaN")),
round(double("INF")),
round(double("-INF")),
round(double("0")),
round(double("-0"))
