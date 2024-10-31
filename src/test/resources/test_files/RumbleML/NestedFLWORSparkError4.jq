(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:3:COLUMN:4:" :)
for $i in parallelize(1)
let $l := (for $i in parallelize(1) return 1)
return $l

(: Job within job :)
