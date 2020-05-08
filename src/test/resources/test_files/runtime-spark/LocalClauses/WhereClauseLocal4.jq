(:JIQS: ShouldRun; Output="(2, 4, 6, 8)" :)
let $sequence := 1 to 10
for $value in $sequence
let $square := $value * 2
where $square lt 10
return $square
