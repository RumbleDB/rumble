(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:3:COLUMN:41:" :)
for $i in parallelize(1)
return (for $i in parallelize(1) return 1)

(: Job within job :)
