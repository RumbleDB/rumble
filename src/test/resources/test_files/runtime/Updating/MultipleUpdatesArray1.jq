(:JIQS: ShouldRun; Output="[ 1, 5, 3, 4 ]" :)
copy $je := [1 to 4]
modify (delete json $je[[2]], insert json 5 into $je at position 3)
return $je