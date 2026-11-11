(:JIQS: ShouldRun; Output="{ "foo" : "bar" }" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify delete json $je.foobar
return $je