(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "bar" : "bar" }" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify rename json $je.foo as "bar"
return $je