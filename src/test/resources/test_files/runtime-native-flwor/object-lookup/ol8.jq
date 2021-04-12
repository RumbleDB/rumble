(:JIQS: ShouldRun; Output="(one, two)" :)
for $i in structured-json-file("../../../queries/difficult-names.json")
let $c := $i."1"
return $c
