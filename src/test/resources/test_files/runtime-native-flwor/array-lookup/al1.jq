(:JIQS: ShouldRun; Output="(Latvian, Nepali, Czech, Greek, Serbian)" :)
for $i in structured-json-lines("../../../queries/conf-ex.json")
let $c := $i.choices[[2]]
return $c