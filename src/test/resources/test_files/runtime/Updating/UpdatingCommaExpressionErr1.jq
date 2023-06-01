(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:8:" :)
copy $je := [1 to 4]
modify (3, delete $je[[1]], delete $je[[2]])
return $je

(: at least one expr is not simple :)