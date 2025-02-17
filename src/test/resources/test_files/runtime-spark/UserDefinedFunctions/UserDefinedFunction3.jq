(:JIQS: ShouldRun; Output="100001" :)
declare function f($x) { $x.foo };
declare function g($x) { $x.bar };
declare function h($x) { count($x) };
declare function i($x) { $x + 1 };
i(h(g(f(for $i in parallelize(1 to 100000) return { "foo" : { "bar" : "foobar" } }))))
