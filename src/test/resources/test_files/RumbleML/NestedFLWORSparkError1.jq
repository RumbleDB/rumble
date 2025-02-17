(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:3:COLUMN:4:" :)
for $i in parallelize(1)
for $k in parallelize(1 to $i)
return $i

