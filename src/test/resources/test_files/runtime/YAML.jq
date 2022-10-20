(:JIQS: ShouldRun; Output="([ 0, "x", { "a" : [ 1, { "b" : 2 }, [ 2.5 ] ], "o" : { "c" : 3 } } ], [ 1, { "b" : 2 }, [ 2.5 ] ], [ 2.5 ])" :)
let $correct := (
    for $item in yaml-doc("../../queries/confusion_sample.yaml")
    let $guess := $item."guess", $target := $item."target"
    where $guess = $target
    group by $target
    return {"Language": $target,
"Correct guesses": count($guess)})

let $incorrect := (
for $item in yaml-doc("../../queries/confusion_sample.yaml")
let $guess := $item."guess", $target := $item."target"
where $guess != $target
group by $target
return {"Language": $target,
"Incorrect guesses": count($guess)})

for $c in $correct, $i in $incorrect
where $c."Language" = $i."Language"
return {"Language": $c."Language",
"Correct guesses": $c."Correct guesses",
"Incorrect guesses": $i."Incorrect guesses"}