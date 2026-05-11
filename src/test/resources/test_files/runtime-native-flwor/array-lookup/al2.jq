(:JIQS: ShouldRun; Output="" :)
for $i in structured-json-lines("../../../queries/conf-ex.json")
let $c := $i.choices[[20]]
return $c