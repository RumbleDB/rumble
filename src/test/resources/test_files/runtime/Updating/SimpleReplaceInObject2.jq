(:JIQS: ShouldRun; Output="{ "foobar" : "foobar", "foo" : "foo" }" :)
copy $je := {"foo": "bar", "foobar": "barfoo"}
modify (replace value of json $je.foo with "foo", replace value of json $je."foobar" with "foobar")
return $je