(:JIQS: ShouldRun; Output="2" :)
declare function f($x, $y) { $x.foo ! ($$ + $y) };
distinct-values(f(for $i in parallelize(1 to 100000) return { "foo" : 1 }, 1))
