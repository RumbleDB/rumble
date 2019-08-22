(:JIQS: ShouldRun; Output="(bar, foo)" :)
for $k in keys(parallelize(for $i in 1 to 10000 return { "foo" : "bar", "bar" : "foobar"}))
order by $k
return $k
