(:JIQS: ShouldCrash; ErrorCode="RBDY0007"; ErrorMetadata="LINE:3:COLUMN:7:" :)
let $data := {"bool" : true, "int": 10}
return delete json $data.bool

(: attempt to modify immutable variable :)