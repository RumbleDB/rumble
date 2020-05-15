(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:2:COLUMN:29:" :)
for $i in parallelize(1) let $l := (for $i in parallelize(1) return 1)
return $i

(: Job within job :)