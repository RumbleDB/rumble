(:JIQS: ShouldCrash; ErrorCode="XUDY0027"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify rename json ().foo as "bar"
return $je

(: target expr is empty seq :)