(:JIQS: ShouldRun; Output="" :)
for $i in structured-json-file("../../../queries/denormalized2.json")
let $c := $i.foo[[2]].bar[[20]]
return $c