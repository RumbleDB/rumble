(:JIQS: ShouldCrash; ErrorCode="JNUP0016"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := [1 to 4]
modify replace value of $je[[0]] with "foo"
return $je

(: selector int is out of range of target array :)