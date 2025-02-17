(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "bar" : "bar" }" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify rename json $je.foo as "bar"
return $je