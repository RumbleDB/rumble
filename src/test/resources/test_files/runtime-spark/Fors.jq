(:JIQS: ShouldRun; Output="(3, 4, 5, 6, 5, 6, 7, 7, 8, 6, 7, 8, 8, 9, 9, 10)" :)
for $i in 1 to 3
for $j in $i to 3
for $k in $j to 4
return $i + $j + $k
