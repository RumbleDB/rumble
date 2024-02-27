(:JIQS: ShouldCrash; ErrorCode="XUST0002"; ErrorMetadata="LINE:2:COLUMN:0:" :)
copy json $je := {"foo" : 1}
modify 3
return $je

(: modify expr is simple :)