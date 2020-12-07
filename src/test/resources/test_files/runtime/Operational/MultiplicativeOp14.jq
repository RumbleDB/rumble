(:JIQS: ShouldRun; Output="(Nan, NaN, INF, -INF, good, good, NaN, NaN, good, NaN, NaN)" :)
3 mod 0e0,
3 mod -0e0,
3 div 0e0,
3 div -0e0,
try { 3 idiv 0e0 } catch FOAR0001 { "good" },
try { 3 idiv -0e0 } catch FOAR0001 { "good" },
0e0 mod 0e0,
0e0 div 0e0,
try { 0e0 idiv 0e0 } catch FOAR0001 { "good" },
3 mod double(0),
double("-0") mod double("-0")
