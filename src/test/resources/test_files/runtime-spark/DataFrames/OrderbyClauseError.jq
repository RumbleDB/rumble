(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:0:" :)
for $j in parallelize((duration("P12Y"), 123, "Hello"))
order by $j
return $j
