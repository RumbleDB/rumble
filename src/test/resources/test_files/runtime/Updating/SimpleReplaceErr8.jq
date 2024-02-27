(:JIQS: ShouldCrash; ErrorCode="JNUP0016"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := {"a" : 1}
modify replace json value of $je."b" with "foo"
return $je

(: selector string does not exist in target object :)