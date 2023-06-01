(:JIQS: ShouldRun; Output="{ "a" : 4, "e" : 5, "g" : 6, "f" : 7 }" :)
copy $je := {"a": 1, "b": 2, "c": 3, "d": 4}
modify (insert "e": 5 into $je, replace value of $je.a with 4, rename $je.b as "f", replace value of $je.b with 7, delete $je.c, delete $je.d, insert "g" : 6 into $je)
return $je