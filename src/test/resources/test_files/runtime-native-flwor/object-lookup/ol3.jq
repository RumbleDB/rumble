(:JIQS: ShouldRun; Output="" :)
for $i in structured-json-lines("../../../queries/denormalized.json")
let $c := $i.foo[[200]].bar
return $c
