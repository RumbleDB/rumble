(:JIQS: ShouldRun; Output="{ "foo" : "bar", "foobar" : "barfoo", "bar" : "foo" }" :)
variable $je := {"foo" : "bar"};
(insert json "foobar": "barfoo" into $je, insert json "bar": "foo" into $je);
$je