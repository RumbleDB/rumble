(:JIQS: ShouldRun; Output="" :)
for $i in structured-json-file("../../../queries/denormalized.json")
let $c := $i.foo[[200]].bar
return $c
