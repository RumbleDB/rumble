(:JIQS: ShouldRun; Output="[ 1, 2, null, 4 ]" :)
copy $je := [1 to 4]
modify replace value of $je[[3]] with ()
return $je