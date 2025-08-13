(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:4:COLUMN:10:" :)
for $i in parallelize((1 to 10), 10)
let $a := date("2023-01-01")
let $b := xs:boolean($a)
return $b
