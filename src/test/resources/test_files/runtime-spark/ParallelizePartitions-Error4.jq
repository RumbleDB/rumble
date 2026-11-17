(:JIQS: ShouldCrash; ErrorCode="FORG0002"; ErrorMetadata="LINE:2:COLUMN:10:" :)
for $f in parallelize((1,2,3), 2147483648)
return $f
