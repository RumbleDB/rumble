(:JIQS: ShouldRun; Output="[ 1, 2, 5, 6 ]" :)
copy $je := [1 to 4]
modify (replace value of $je[[3]] with 5, replace value of $je[[4]] with 6)
return $je