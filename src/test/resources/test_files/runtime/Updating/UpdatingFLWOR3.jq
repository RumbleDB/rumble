(:JIQS: ShouldRun; Output="([ 0 ], [ 1 ], [ 0 ], [ 1 ])" :)
copy $je := for $i in (1 to 4)
                 return [$i mod 2]
modify
    for $l in $je
    return
        if($l[[1]] eq 0)
        then
            replace value of $l[[1]] with 1
        else
            replace value of $l[[1]] with 0
return $je