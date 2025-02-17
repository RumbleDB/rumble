(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:8:" :)
copy $je := [1 to 4]
modify (3, delete json $je[[1]], delete json $je[[2]])
return $je

(: at least one expr is not simple :)