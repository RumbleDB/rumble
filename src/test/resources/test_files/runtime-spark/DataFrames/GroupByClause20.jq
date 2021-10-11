declare	type local:a as { "foo" : "string" };

for $x in validate type	local:a* { { "foo" : "bar" }, { "foo" : "foobar" } }
group by $a := 1
let $c := count($x)
let $d := count($x[$$.foo eq "bar"])
return { "c" : $c, "d" : $d }