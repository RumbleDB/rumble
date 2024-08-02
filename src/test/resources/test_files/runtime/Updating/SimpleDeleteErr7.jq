(:JIQS: ShouldCrash; ErrorCode="JNUP0016"; ErrorMetadata="LINE:3:COLUMN:33:" :)
copy $je := {"a" : 1}
modify (insert json "b" : 4 into $je, delete json $je.b)
return $je

(: selector string does not exist in target object in snapshot :)