(:JIQS: ShouldRun; Output="[ 1, 2, 3, 4, 5 ]" :)
copy $je := [1 to 4]
modify append json 5 into $je
return $je
