(:JIQS: ShouldRun; Output="(very tricky, unexpected)" :)
for $i in structured-json-lines("../../../queries/difficult-names.json")
let $c := $i."quotes\""
return $c
