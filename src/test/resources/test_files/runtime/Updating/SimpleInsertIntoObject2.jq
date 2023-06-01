(:JIQS: ShouldRun; Output="{ "foo" : "bar", "foobar" : "barfoo", "bar" : "foo" }" :)
copy $je := {"foo" : "bar"}
modify (insert "foobar": "barfoo" into $je, insert "bar": "foo" into $je)
return $je