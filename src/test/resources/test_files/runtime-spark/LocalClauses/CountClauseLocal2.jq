(:JIQS: ShouldRun; Output="([ 0, [ 1, 3 ] ], [ 1, [ 2 ] ])" :)
for $i in (10, 9, 8)
count $j
let $k := $i mod 2
group by $k
return [ $k, [ $j ] ]
