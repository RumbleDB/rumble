(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:3:COLUMN:0:" :)
for $i in parallelize(1)
group by $l := (for $i in parallelize(1) return 1)
return $i
