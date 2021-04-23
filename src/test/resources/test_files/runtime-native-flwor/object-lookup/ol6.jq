(:JIQS: ShouldRun; Output="(impressive, amazing)" :)
for $i in structured-json-file("../../../queries/difficult-names.json")
let $c := $i."include spaces"
return $c
