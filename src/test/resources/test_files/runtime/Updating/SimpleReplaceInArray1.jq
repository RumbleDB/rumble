(:JIQS: ShouldRun; Output="[ 1, 2, 5, 4 ]" :)
copy json $je := [1 to 4]
modify replace json value of $je[[3]] with 5
return $je