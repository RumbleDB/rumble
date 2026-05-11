(:JIQS: ShouldRun; Output="[ 1, 2, 4 ]" :)
copy $je := [1 to 4]
modify (replace value of json $je[[3]] with 6, delete json $je[[3]])
return $je