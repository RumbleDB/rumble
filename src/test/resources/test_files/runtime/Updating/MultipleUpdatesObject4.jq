(:JIQS: ShouldRun; Output="{ "a" : 4, "e" : 5, "g" : 6, "f" : 7 }" :)
copy json $je := {"a": 1, "b": 2, "c": 3, "d": 4}
modify (insert json "e": 5 into $je, replace json value of $je.a with 4, rename json $je.b as "f", replace json value of $je.b with 7, delete json $je.c, delete json $je.d, insert json "g" : 6 into $je)
return $je