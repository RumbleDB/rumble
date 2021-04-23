(:JIQS: ShouldRun; Output="(Russian, Russian, Czech, Serbian, Serbian)" :)
for $i in structured-json-file("../../../queries/conf-ex.json")
let $c := $i.target
return $c
