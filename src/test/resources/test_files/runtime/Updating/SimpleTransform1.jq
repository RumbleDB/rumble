(:JIQS: ShouldRun; Output="[ 2, 3, 4 ]" :)
copy $je := [1 to 4]
modify delete json $je[[1]]
return $je
