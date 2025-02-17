(:JIQS: ShouldRun; Output="(2, 2, 3, 2, 3, 4)" :)
for $i in 1 to 3
for $j in (for $i in 1 to $i
          return $i + 1)
return $j
