(:JIQS: ShouldRun; Output="[ 1, 2, 3, 5, 4 ]" :)
copy $je := [1 to 4]
modify insert json 5 into $je at position 4
return $je
