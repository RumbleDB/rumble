(:JIQS: ShouldRun; Output="[ 1, 4 ]" :)
copy $je := [1 to 4]
modify (delete $je[[3]], delete $je[[2]])
return $je