(:JIQS: ShouldRun; Output="(Lao, Croatian, Maori, German, Dari, Russian, Russian, Korean, Kannada, Sinhalese)" :)
for $idx in (1,3,5)
return
for $i in structured-json-file("../../../queries/conf-ex.json")
let $c := $i.choices[[$idx]]
return $c