(:JIQS: ShouldCrash; ErrorCode="XUDY0027"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify replace value of ().foo with "foo"
return $je

(: target expr is empty seq :)