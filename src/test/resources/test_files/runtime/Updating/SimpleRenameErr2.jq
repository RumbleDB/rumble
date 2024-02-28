(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify rename json $je.foo as (rename json $je.foo as "bar")
return $je

(: source expr is not simple :)