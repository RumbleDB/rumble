(:JIQS: ShouldRun; Output="{ "b" : 2, "c" : 3, "d" : 4, "e" : 5 }" :)
copy $je := {"a": 1, "b": 2, "c": 3, "d": 4}
modify (delete $je.a, insert "e": 5 into $je)
return $je