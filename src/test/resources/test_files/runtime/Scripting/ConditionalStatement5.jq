(:JIQS: ShouldRun; Output="{ "b" : 2, "c" : 3, "d" : 4, "e" : 5 }" :)
variable $je := {"a": 1, "b": 2, "c": 3, "d": 4};
if ($je.a eq 1) then {
    (delete json $je.a, insert json "e": 5 into $je, replace json value of $je.a with 6, rename json $je.a as "f");
}
else {
    (delete json $je.a, insert json "e": 8 into $je, replace json value of $je.a with 16, rename json $je.a as "g");
}
$je