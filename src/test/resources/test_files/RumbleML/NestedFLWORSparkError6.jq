(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:3:COLUMN:0:" :)
for $i in parallelize(1)
order by (for $i in parallelize(1) return 1)
return $i

(: Job within job :)
