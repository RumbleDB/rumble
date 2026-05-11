(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:3:COLUMN:4:" :)
for $i in parallelize(1)
for $l in (for $k in parallelize($i) return $k)
return $l
