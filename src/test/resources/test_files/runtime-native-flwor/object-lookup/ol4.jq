(:JIQS: ShouldRun; Output="" :)
for $i in structured-json-file("../../../queries/denormalized.json")
let $c := $i.wrongkey
return $c
