(:JIQS: ShouldRun; Output="" :)
for $k in keys(keys(parallelize(for $i in 1 to 10000 return { "foo" : "bar", "bar" : "foobar"})))
order by $k
return $k
