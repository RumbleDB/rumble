(:JIQS: ShouldRun; Output="[ 9, 5, 7, 8, 6 ]" :)
copy $je := [1 to 4]
modify (delete $je[[1]], delete $je[[3]], replace value of $je[[2]] with 5, replace value of $je[[4]] with 6, insert 7 into $je at position 4, insert 8 into $je at position 4, insert 9 into $je at position 1)
return $je