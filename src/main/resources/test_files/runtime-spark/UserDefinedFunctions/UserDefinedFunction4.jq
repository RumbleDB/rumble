(:JIQS: ShouldRun; Output="1" :)
declare function f($x as integer) { $x };
f(for $i in parallelize(1 to 100000) where $i eq 1 return $i)
