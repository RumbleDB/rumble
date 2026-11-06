(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:2:COLUMN:20:" :)
for $i as string in parallelize(("hello", "world", "!", 3e4))
return $i
