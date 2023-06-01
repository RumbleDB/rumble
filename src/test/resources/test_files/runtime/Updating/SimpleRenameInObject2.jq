(:JIQS: ShouldRun; Output="{ "barfoo" : "barfoo", "bar" : "bar" }" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify (rename $je.foo as "bar", rename $je.foobar as "barfoo")
return $je