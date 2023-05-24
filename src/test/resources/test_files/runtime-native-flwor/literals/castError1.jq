(:JIQS: ShouldCrash; ErrorCode="FORG0006"; ErrorMetadata="LINE:4:COLUMN:18:" :)
for $i in parallelize((1 to 10), 10)
let $a := date("2023-01-01")
let $b := boolean($a)
return $b
