(:JIQS: ShouldRun; Output="({ "target" : "Czech", "guess" : "Czech" }, { "target" : "Russian", "guess" : "Russian" }, { "target" : "Russian", "guess" : "Latvian" }, { "target" : "Serbian", "guess" : "Serbian" }, { "target" : "Serbian", "guess" : "Greek" })" :)
for $i in json-file("./src/test/resources/test_data/conf-ex.json")
order by $i."target", $i."guess" descending
return {"target" : $i."target", "guess" : $i."guess"}