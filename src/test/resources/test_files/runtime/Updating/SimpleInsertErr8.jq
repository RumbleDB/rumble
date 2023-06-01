(:JIQS: ShouldCrash; ErrorCode="JNDY0003"; ErrorMetadata="LINE:3:COLUMN:27:" :)
copy $je := {"a" : 1}
modify insert "b" : 3 into {"a" : 1, "a" : 2}
return $je

(: content expr does not evaluate to object :)