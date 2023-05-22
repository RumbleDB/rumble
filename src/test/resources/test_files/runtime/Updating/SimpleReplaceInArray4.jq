(:JIQS: ShouldRun; Output="[ 1, 2, null, 4 ]" :)
copy json $je := [1 to 4]
modify replace json value of $je[[3]] with ()
return $je