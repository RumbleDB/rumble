(:JIQS: ShouldRun; Output="(1, 1, 1, 1, 1)" :)
for $i in structured-json-lines("../../../queries/conf-ex.json")
let $g := 1
return $g