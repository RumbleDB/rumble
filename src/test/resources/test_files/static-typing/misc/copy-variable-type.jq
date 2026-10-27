(:JIQS: ShouldRun; Output="(2, 1)" :)
copy $a := {"a": 1}, $b := [1]
modify insert json {"b": 2} into $a
return (($a is statically object).b, ($b is statically array)[[1]])
