(:JIQS: ShouldRun; Output="3" :)
for $x in parallelize(("foo", "bar"))
group by $y as integer := string-length($x)
return $y
