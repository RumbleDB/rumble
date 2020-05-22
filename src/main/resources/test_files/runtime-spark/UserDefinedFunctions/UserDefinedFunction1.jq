(:JIQS: ShouldRun; Output="100000" :)
declare function f($x) { $x.foo };
count(f(for 1 to 100000 return { "foo" : "bar" }))
