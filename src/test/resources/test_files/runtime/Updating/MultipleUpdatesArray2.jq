(:JIQS: ShouldRun; Output="[ 1, 2, 5, 4 ]" :)
copy $je := [1 to 4]
modify (delete json $je[[3]], insert json 5 into $je at position 3)
return $je