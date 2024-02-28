(:JIQS: ShouldRun; Output="{ "foo" : "bar", "foobar" : "barfoo" }" :)
copy json $je := {"foo": "bar"}
modify insert json "foobar": "barfoo" into $je
return $je
