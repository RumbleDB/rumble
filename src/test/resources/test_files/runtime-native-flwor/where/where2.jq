(:JIQS: ShouldRun; Output="(1, include spaces)" :)
for $i in structured-json-lines("../../../queries/difficult-names.json")
where true
return $i.keyToUse