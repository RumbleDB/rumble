(:JIQS: ShouldCrash; ErrorCode="JNUP0016"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify rename json $je."bar" as "barbar"
return $je

(: selector string does not exist in target object :)