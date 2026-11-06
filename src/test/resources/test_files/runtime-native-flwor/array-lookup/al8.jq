(:JIQS: ShouldRun; Output="(3, 9, 3, 9, 3, 9, 3, 9, 3, 9, 3, 9, 3, 9)" :)
for $i in structured-json-lines("../../../queries/denormalized2.json")
let $c := $i.foo[[2]].bar[[1]].foobar
return $c