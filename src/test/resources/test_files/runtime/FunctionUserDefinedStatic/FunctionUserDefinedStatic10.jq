(:JIQS: ShouldRun; Output="(3, 2002.3)" :)
declare function price ($x as decimal?, $y as double) as double* { $x+$y };
price(1,2),
price((), 3),
price(2.3, 2e3)

(: type promotion :)
