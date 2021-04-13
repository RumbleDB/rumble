(:JIQS: ShouldRun; Output="(1, include spaces)" :)
for $i in structured-json-file("../../../queries/difficult-names.json")
where true
return $i.keyToUse