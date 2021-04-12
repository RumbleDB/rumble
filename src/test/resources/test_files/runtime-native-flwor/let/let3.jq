(:JIQS: ShouldRun; Output="(1, 1, 1, 1, 1)" :)
for $i in structured-json-file("../../../queries/conf-ex.json")
let $g := 1
return $g