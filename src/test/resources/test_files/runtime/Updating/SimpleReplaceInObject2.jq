(:JIQS: ShouldRun; Output="{ "foobar" : "foobar", "foo" : "foo" }" :)
copy json $je := {"foo": "bar", "foobar": "barfoo"}
modify (replace json value of $je.foo with "foo", replace json value of $je."foobar" with "foobar")
return $je