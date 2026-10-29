(:JIQS: ShouldRun; Output="3" :)
(: Partial application must preserve distributed execution of the remaining argument. :)
let $f := (subsequence#3)(?, 1, 3)
return count($f(parallelize(1 to 100001)))
