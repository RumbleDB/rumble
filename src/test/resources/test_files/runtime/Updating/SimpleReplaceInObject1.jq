(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "foo" : "foo" }" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify replace value of json $je.foo with "foo"
return $je