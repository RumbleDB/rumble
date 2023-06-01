(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify rename $je.foo as (rename $je.foo as "bar")
return $je

(: source expr is not simple :)