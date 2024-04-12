(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "foo" : "foo" }" :)
variable $je := {"foo": "bar", "foobar": "barfoo"};
replace json value of $je.foo with "foo";
$je