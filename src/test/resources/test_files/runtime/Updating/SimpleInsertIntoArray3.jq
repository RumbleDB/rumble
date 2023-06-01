(:JIQS: ShouldRun; Output="[ 1, 2, 5, 5, 3, 4 ]" :)
copy $je := [1 to 4]
modify (insert 5 into $je at position 3, insert 5 into $je at position 3)
return $je