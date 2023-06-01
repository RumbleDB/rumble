(:JIQS: ShouldRun; Output="{ "foo" : "bar", "foobar" : "barfoo" }" :)
copy $je := {"foo": "bar"}
modify insert "foobar": "barfoo" into $je
return $je
