(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
for $i in parallelize(1)
return (for $i in parallelize(1) return 1)

(: Job within job :)