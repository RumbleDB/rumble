(:JIQS: ShouldCrash; ErrorCode="JNUP0006"; ErrorMetadata="LINE:3:COLUMN:27:" :)
copy $je := {"a": 1, "b": 2, "c": 3, "d": 4}
modify (delete json $je.a, insert json "a" : 10 into $je)
return $je

(: selector key already exists in object in snapshot :)