(:JIQS: ShouldRun; Output="[ 1, 2, 3, 5, 4, 6 ]" :)
copy $je := [1 to 4]
modify (insert 5 into $je at position 4, insert 6 into $je at position 5)
return $je
