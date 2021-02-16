(:JIQS: ShouldRun; Output="(Russian, Russian, Korean, Kannada, Sinhalese)" :)
for $i in structured-json-file("../../../queries/conf-ex.json")
let $c := $i.choices[[2 * 2 - 1]]
return $c