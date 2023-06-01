(:JIQS: ShouldRun; Output="{ "foo" : "bar" }" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify delete $je.foobar
return $je