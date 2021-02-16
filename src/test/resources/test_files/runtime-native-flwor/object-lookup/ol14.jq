(:JIQS: ShouldRun; Output="(oh cmon, why?)" :)
for $i in structured-json-file("../../../queries/backtick.json")
let $c := $i."backtick`"
return $c
