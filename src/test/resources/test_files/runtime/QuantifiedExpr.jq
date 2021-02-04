(:JIQS: ShouldRun; Output="(true, false, true, false, true, true, true, true)" :)
every $i in 1 to 10 satisfies $i lt 15,
every $i in 1 to 5, $j in 1 to 5 satisfies $i eq $j,
every $i in 1 to 5, $j in 1 to $i, $k in 1 to $j satisfies $k le $j and $j le $i,
some $i in 1 to 3 satisfies $i gt 4,
some $a in (1,2,"a") satisfies $a,
every $a in (1,2,"a") satisfies $a,
some $foo in 1 satisfies 1,
some $i in (0, 2, 3) satisfies count($i)