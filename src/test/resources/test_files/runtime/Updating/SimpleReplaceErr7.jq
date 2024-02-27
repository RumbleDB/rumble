(:JIQS: ShouldCrash; ErrorCode="JNUP0016"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := [1 to 4]
modify replace json value of $je[[0]] with "foo"
return $je

(: selector int is out of range of target array :)