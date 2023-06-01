(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "foo" : "foo" }" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify replace value of $je.foo with "foo"
return $je