(:JIQS: ShouldCrash; ErrorCode="XPST0005"; ErrorMetadata="LINE:3:COLUMN:10:" :)
for $i in structured-json-file("../../../queries/denormalized.json")
let $c := $i.wrongkey
return $c
