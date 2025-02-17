(:JIQS: ShouldRun; Output="" :)
for $i in structured-json-file("../../../queries/conf-ex.json")
let $c := $i.choices[[20]]
return $c