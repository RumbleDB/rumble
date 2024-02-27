(:JIQS: ShouldRun; Output="{ "barfoo" : "barfoo", "bar" : "bar" }" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify (rename json $je.foo as "bar", rename json $je.foobar as "barfoo")
return $je