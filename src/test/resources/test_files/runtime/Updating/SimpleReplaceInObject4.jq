(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "foo" : null }" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify replace json value of $je.foo with ()
return $je