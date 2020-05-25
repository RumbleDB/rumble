(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:0:" :)
for $j as hexBinary in (hexBinary("0123456789abcdef"), hexBinary("AaBb"), hexBinary("aAbB"), hexBinary(()))
order by $j
return $j