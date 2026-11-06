(:JIQS: ShouldRun; Output="{ }" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify (delete json $je.foobar, delete json $je.foo)
return $je