jsoniq version "1.0";
import module namespace f = "http://expath.org/ns/file";

(:for $i in (1,2,3), $j in (1,2,3)
return $i + $j:)

(:for $item in jn:parse-json(f:read-text("confusion_sample.json"))
return $item:)

(:for $item in jn:parse-json(f:read-text("confusion_sample.json"))
where $item."country" = "AU"
return $item:)

(:for $item in jn:parse-json(f:read-text("confusion_sample.json"))
where $item."country" = "AU"
return $item:)

(:for $item in jn:parse-json(f:read-text("confusion_sample.json"))
where $item."choices"[[1]] = "Bulgarian"
return $item:)

(:for $item in jn:parse-json(f:read-text("confusion_sample.json"))
let $guess := $item."guess", $target := $item."target"
where $guess = $target
return $item:)


(:for $item in jn:parse-json(f:read-text("confusion_sample.json"))
where  $item."target" = "Bulgarian" and $item.target = $item.guess
order by $item.date
return $item:)

(:for $item in jn:parse-json(f:read-text("confusion_sample.json"))
where  $item."target" eq "Bulgarian"
group by $guess := $item.guess
return { "guess" :$guess,
         "count": count($item)}:)

(:for $item in jn:parse-json(f:read-text("confusion_sample.json"))
let $guess := $item."guess", $target := $item."target"
where $guess = $target
group by $target
return {"Language": $target,
        "Correct guesses": count($guess)}:)


let $correct := (for $item in jn:parse-json(f:read-text("confusion_sample.json"))
let $guess := $item."guess", $target := $item."target"
where $guess = $target
group by $target
return {"Language": $target,
        "Correct guesses": count($guess)})
let $incorrect := (for $item in jn:parse-json(f:read-text("confusion_sample.json"))
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





