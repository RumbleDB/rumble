(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:25:" :)
for $x in ("foo", "bar")
group by $y as string := string-length($x)
return $y
