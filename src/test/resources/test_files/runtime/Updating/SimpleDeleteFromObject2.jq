(:JIQS: ShouldRun; Output="{ }" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify (delete $je.foobar, delete $je.foo)
return $je