(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "foo" : "foo" }" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify replace json value of $je.foo with "foo"
return $je