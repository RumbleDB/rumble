(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:2:COLUMN:10:" :)
for $f in parallelize((1,2,3), ())
return $f

(: Non-integer partition given :)
