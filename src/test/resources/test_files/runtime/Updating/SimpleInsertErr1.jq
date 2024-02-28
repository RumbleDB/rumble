(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := [1 to 4]
modify insert json (insert json 5 into $je at position 4) into $je at position 4
return $je

(: source expr is not simple :)