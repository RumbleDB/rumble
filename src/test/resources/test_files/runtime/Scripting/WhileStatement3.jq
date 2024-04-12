(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "foo" : "foo" }" :)
variable $je := {"foo": "bar", "foobar": "barfoo"};
while ($je.foo eq "bar") {
    replace json value of $je.foo with "foo";
}
$je