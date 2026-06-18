(:JIQS: ShouldRun; Output="(1, 9, 2, 3, 4)" :)
let $partial := insert-before(parallelize(1 to 4), ?, 9)
return $partial(2)
