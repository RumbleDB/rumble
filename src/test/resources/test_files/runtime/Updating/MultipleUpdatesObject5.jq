(:JIQS: ShouldRun; Output="{ "b" : 2, "c" : 3, "d" : 4 }" :)
copy $je := {"a": 1, "b": 2, "c": 3, "d": 4}
modify (delete json $je.a, replace value of json $je.a with 5)
return $je