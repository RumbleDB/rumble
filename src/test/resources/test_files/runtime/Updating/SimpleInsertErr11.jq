(:JIQS: ShouldCrash; ErrorCode="JNUP0006"; ErrorMetadata="LINE:3:COLUMN:22:" :)
copy $je := {"a": 1, "b": 2, "c": 3, "d": 4}
modify (delete $je.a, insert "a" : 10 into $je)
return $je

(: selector key already exists in object in snapshot :)