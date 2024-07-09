(:JIQS: ShouldRun; Output="{ "b" : 2, "c" : 3, "d" : 4, "e" : 8, "g" : 16 }" :)
variable $je := {"a": 1, "b": 2, "c": 3, "d": 4};
replace json value of $je.a with 3;
if ($je.a eq 1 and $je.b eq 2) then {
    (insert json "e": 5 into $je, replace json value of $je.a with 6, rename json $je.a as "f");
}
else {
    (insert json "e": 8 into $je, replace json value of $je.a with 16, rename json $je.a as "g");
}
$je