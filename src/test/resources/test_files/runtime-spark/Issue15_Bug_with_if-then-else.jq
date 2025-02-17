(:JIQS: ShouldRun; Output="(1, 2, 3, 4, -5, -6, -7, -8, -9, -10)" :)
for $x in 1 to 10
return if ($x lt 5) then $x
               else -$x
