(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify replace json value of (replace json value of $je.foo with "foo").foo with "foo"
return $je

(: target expr is not simple :)