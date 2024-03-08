(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "foo" : [ "foo", "bar" ] }" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify replace json value of $je.foo with ("foo", "bar")
return $je