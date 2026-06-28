(:JIQS: ShouldRun; Output="(3, false)" :)
let $partial := max#1(?)
return
(
    $partial((1, 2, 3)),
    exists(function-name($partial))
)
