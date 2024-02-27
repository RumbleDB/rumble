(:JIQS: ShouldRun; Output="[ 1, 2, 5, 6, 4 ]" :)
copy json $je := [1 to 4]
modify (insert json 5 into $je at position 3, replace json value of $je[[3]] with 6)
return $je