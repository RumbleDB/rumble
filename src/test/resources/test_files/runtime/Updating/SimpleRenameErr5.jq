(:JIQS: ShouldCrash; ErrorCode="JNUP0007"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify rename json $je.$je as "bar"
return $je

(: selector expr does not evaluate to String :)