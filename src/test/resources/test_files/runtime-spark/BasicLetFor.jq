(:JIQS: ShouldRun; Output="(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)" :)
let $sequence := 1 to 10
for $value in $sequence
let $square := $value * 2
return $square
