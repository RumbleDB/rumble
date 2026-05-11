(:JIQS: ShouldRun; Output="(Russian, Russian, Czech, Serbian, Serbian)" :)
declare variable $k := "target";
for $i in structured-json-lines("../../../queries/conf-ex.json")
let $c := $i.$k
return $c
