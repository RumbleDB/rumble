(:JIQS: ShouldRun; Output="{ "foobar" : "barfoo", "foo" : "foo" }" :)
variable $je := {"foo": "bar", "foobar": "barfoo"};
while ($je.foo eq "bar") {
    replace value of json $je.foo with "foo";
}
$je