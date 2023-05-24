(:JIQS: ShouldCrash; ErrorCode="FORG0001"; ErrorMetadata="LINE:1:COLUMN:0:" :)
for $i in parallelize((1 to 10), 10)
let $a := xs:boolean("falsea")
return $a
