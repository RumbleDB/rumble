(:JIQS: ShouldRun; Output="{ "b" : 2, "c" : 3, "d" : 4 }" :)
copy $je := {"a": 1, "b": 2, "c": 3, "d": 4}
modify (delete $je.a, replace value of $je.a with 5)
return $je