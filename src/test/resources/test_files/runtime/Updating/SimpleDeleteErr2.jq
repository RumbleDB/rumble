(:JIQS: ShouldCrash; ErrorCode="JNUP0008"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := { "a" : 1, "b" : 2, "c" : 3 }
modify delete json false[[1]]
return $je

(: target expr does not evaluate to array or object :)