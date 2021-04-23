(:JIQS: ShouldRun; Output="(Latvian, Russian, Czech, Greek, Serbian)" :)
for $i in structured-json-file("../../../queries/conf-ex.json")
let $g := $i.guess
return $g