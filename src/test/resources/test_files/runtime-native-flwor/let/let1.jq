(:JIQS: ShouldRun; Output="(Latvian, Russian, Czech, Greek, Serbian)" :)
for $i in structured-json-lines("../../../queries/conf-ex.json")
let $g := $i.guess
return $g