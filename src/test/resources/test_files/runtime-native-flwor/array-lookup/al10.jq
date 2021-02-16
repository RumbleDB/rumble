(:JIQS: ShouldRun; Output="(Russian, Russian, Korean, Kannada, Sinhalese)" :)
declare variable $idx := 3;
for $i in structured-json-file("../../../queries/conf-ex.json")
let $c := $i.choices[[$idx]]
return $c