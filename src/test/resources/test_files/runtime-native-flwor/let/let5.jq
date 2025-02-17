(:JIQS: ShouldRun; Output="(Lao, Croatian, Maori, German, Dari)" :)
for $i in structured-json-file("../../../queries/conf-ex.json")
let $g := $i.choices[[1]]
return $g