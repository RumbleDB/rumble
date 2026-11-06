(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "foo" : "foo" }" :)
variable $je := {"foo": "bar", "foobar": "barfoo"};
replace value of json $je.foo with "foo";
$je