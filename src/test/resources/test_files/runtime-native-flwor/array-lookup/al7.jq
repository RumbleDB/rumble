(:JIQS: ShouldRun; Output="" :)
for $i in structured-json-file("../../../queries/denormalized2.json")
let $c := $i.foo[[20]].bar[[1]]
return $c