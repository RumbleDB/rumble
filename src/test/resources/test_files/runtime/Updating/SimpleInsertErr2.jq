(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := [1 to 4]
modify insert json 5 into (insert json 5 into $je at position 4) at position 4
return $je

(: target expr is not simple :)