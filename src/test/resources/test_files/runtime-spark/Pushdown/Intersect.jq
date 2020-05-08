(:JIQS: ShouldRun; Output="(1000, foo, [ [ "bar" ], "foobar" ])" :)
count(intersect(parallelize(for $i in 1 to 1000 return {"foo":"bar","foobar":"foo"})).foo[]),
intersect(parallelize(for $i in 1 to 1000 return {"foo":"bar","foobar":"foo"})).foobar[][500],
intersect(for $i in parallelize(1 to 1000) return { "foo" : [ [ "bar" ], "foobar" ] }).foo[][500]
