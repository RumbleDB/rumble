(:JIQS: ShouldCrash; ErrorCode="JNUP0016"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := {"a" : 1}
modify delete json $je.b
return $je

(: selector string does not exist in target object :)