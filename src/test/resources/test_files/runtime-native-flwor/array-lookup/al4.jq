(:JIQS: ShouldRun; Output="(3, 9, 3, 9, 3, 9, 3, 9, 3, 9, 3, 9, 3, 9)" :)
for $i in structured-json-lines("../../../queries/denormalized.json")
let $c := $i.foo[[2]].bar
return $c