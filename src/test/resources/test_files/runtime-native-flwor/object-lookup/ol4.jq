(:JIQS: ShouldRun; Output="" :)
for $i in structured-json-lines("../../../queries/denormalized.json")
let $c := $i.wrongkey
return $c
