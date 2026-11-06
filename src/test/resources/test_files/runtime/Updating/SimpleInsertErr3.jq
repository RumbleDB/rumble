(:JIQS: ShouldCrash; ErrorCode="XUDY0027"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := [1 to 4]
modify insert json 5 into () at position 4
return $je

(: target expr is empty seq :)