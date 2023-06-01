(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:2:COLUMN:0:" :)
copy $je := {"foo" : 1}
modify rename $je.foo as "bar"
return rename {"bar" : 1}.bar as "bar"

(: return expr is not simple :)