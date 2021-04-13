(:JIQS: ShouldRun; Output="{ "1" : "one", "a" : [ 10, 20 ], "include spaces" : "impressive", "indexToUse" : 1, "keyToUse" : "1", "quotes\"" : "very tricky" }" :)
for $i in structured-json-file("../../../queries/difficult-names.json")
where $i.indexToUse eq 1
return $i