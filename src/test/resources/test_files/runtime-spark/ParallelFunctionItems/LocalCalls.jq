(:JIQS: ShouldRun; Output="(2, 3)" :)
let $f := function($rows as integer*) as integer { count($rows) }
return ($f((1, 2)), $f((1, 2, 3)))
