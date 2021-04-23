(:JIQS: ShouldRun; Output="(Latvian, Russian, Czech, Greek, Serbian, Russian, Russian, Czech, Serbian, Serbian)" :)
for $k in ("guess", "target", "missing")
return
for $i in structured-json-file("../../../queries/conf-ex.json")
let $c := $i.$k
return $c
