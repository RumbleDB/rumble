(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := [1 to 4]
modify delete json (delete json $je[[1]])[[1]]
return $je

(: source expr is not simple :)