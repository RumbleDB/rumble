(:JIQS: ShouldCompile :)
for $x in ("foo", "bar")
group by $y as integer := string-length($x)
return $y;