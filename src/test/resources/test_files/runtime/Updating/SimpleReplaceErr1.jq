(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify replace json value of $je.foo with (replace json value of $je.foo with "foo")
return $je

(: source expr is not simple :)