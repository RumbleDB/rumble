(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "foo" : null }" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify replace value of $je.foo with ()
return $je