(:JIQS: ShouldRun; Output="(2, 3, 4)" :)
let $partial := tail(?)
let $partial-again := $partial(?)
return $partial-again(1 to 4)
