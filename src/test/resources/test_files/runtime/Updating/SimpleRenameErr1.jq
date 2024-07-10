(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify rename json (rename json $je.foo as "bar").foo as "bar"
return $je

(: target expr is not simple :)