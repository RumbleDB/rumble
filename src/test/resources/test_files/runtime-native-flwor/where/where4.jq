(:JIQS: ShouldRun; Output="(1, include spaces)" :)
for $i in structured-json-file("../../../queries/difficult-names.json")
where $i.a[[1]] eq 10
return $i.keyToUse