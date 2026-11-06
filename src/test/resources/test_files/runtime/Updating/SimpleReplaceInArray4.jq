(:JIQS: ShouldRun; Output="[ 1, 2, null, 4 ]" :)
copy $je := [1 to 4]
modify replace value of json $je[[3]] with ()
return $je