(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:1:COLUMN:0:" :)
for $i in parallelize((1 to 10), 10)
let $a := xs:boolean("falsea")
return $a
