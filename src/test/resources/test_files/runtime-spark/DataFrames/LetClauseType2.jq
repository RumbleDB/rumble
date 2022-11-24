(:JIQS: ShouldRun; Output="(3, 3)" :)
for $x in parallelize(("foo", "bar"))
let $y as int := string-length($x) cast as int
return $y
