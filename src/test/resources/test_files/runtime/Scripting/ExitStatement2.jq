(:JIQS: ShouldRun; Output="{ "b" : 2, "c" : 3, "d" : 4, "e" : 5 }" :)
variable $je := {"a": 1, "b": 2, "c": 3, "d": 4};
(delete json $je.a, insert json "e": 5 into $je, replace value of json $je.a with 6, rename json $je.a as "f");
exit returning $je;
(delete json $je.a, insert json "l": 6 into $je, replace value of json $je.a with 8, rename json $je.a as "k")