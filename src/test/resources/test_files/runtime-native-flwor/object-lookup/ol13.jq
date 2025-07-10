(:JIQS: ShouldRun; Output="(one, amazing)" :)
for $i in structured-json-lines("../../../queries/difficult-names.json")
let $k := $i.keyToUse
let $c := $i.$k
return $c
