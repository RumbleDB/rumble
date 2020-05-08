(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:25:" :)
for $j as integer in (1 to 10)
group by $k as string := $j
return $k
