(:JIQS: ShouldCrash; ErrorCode="JNUP0008"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := [1 to 4]
modify insert json 5 into false at position 4
return $je

(: target expr must be array :)