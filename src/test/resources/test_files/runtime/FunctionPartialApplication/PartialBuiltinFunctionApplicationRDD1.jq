(:JIQS: ShouldRun; Output="(2, 3, 4, 5)" :)
let $partial := tail(?)
return $partial(parallelize(1 to 5))
