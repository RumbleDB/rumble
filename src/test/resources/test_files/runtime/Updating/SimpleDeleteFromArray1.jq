(:JIQS: ShouldRun; Output="[ 1, 3, 4 ]" :)
copy $je := [1 to 4]
modify delete json $je[[2]]
return $je
