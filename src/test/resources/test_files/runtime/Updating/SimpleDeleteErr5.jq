(:JIQS: ShouldCrash; ErrorCode="JNUP0016"; ErrorMetadata="LINE:3:COLUMN:8:" :)
copy $je := [1 to 4]
modify (delete $je[[0]], delete $je[[5]])
return $je

(: selector int is out of range of target array :)