(:JIQS: ShouldRun; Output="(Latvian, Nepali, Czech, Greek, Serbian)" :)
declare variable $idx := 3;
for $i in structured-json-file("../../../queries/conf-ex.json")
let $c := $i.choices[[$idx - 1]]
return $c