(:JIQS: ShouldRun; Output="(3, 3)" :)
for $x in parallelize(("foo", "bar"))
let $y as integer := string-length($x)
return $y
