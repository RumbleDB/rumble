(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "foo" : [ "foo", "bar" ] }" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify replace value of json $je.foo with ("foo", "bar")
return $je