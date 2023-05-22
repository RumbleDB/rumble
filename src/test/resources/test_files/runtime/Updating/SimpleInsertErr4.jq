(:JIQS: ShouldCrash; ErrorCode="JNUP0008"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := {"foo": "bar"}
modify insert json "foobar": "barfoo" into false
return $je

(: target expr must be object :)