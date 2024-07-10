(:JIQS: ShouldRun; Output="[ 1, 2, [ 5, 6 ], 4 ]" :)
copy $je := [1 to 4]
modify replace value of json $je[[3]] with (5, 6)
return $je