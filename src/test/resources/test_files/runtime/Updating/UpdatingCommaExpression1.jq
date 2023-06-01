(:JIQS: ShouldRun; Output="[ 3, 4 ]" :)
copy $je := [1 to 4]
modify ((), delete $je[[1]], delete $je[[2]])
return $je
