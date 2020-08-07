(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:22:COLUMN:44:" :)
let $correct := (
    for $item in jn:parse-json(f:read-text("confusion_sample.json"))
    let $guess := $item."guess", $target := $item."target"
    where $guess = $target
    group by $target
    return {"Language": $target,
"Correct guesses": count($guess)})

let $incorrect := (
for $item in jn:parse-json(f:read-text("confusion_sample.json"))
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