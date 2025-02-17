(:JIQS: ShouldRun; Output="3" :)
for $x in ("foo", "bar")
group by $y as integer := string-length($x)
return $y
