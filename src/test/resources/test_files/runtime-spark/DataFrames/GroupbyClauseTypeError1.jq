(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:2:COLUMN:29:" :)
for $x in parallelize("foo", "bar")
group by $y as string := string-length($x)
return $y
