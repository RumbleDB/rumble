(:JIQS: ShouldRun; Output="{ "foo" : "bar", "foobar" : "barfoo", "bar" : "foo" }" :)
copy $je := {"foo" : "bar"}
modify (insert json "foobar": "barfoo" into $je, insert json "bar": "foo" into $je)
return $je