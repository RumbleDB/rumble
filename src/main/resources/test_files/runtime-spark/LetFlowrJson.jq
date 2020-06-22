(:JIQS: ShouldRun; Output="({ "target" : "Russian", "guess" : "Latvian" }, { "target" : "Russian", "guess" : "Russian" }, { "target" : "Czech", "guess" : "Czech" }, { "target" : "Serbian", "guess" : "Greek" }, { "target" : "Serbian", "guess" : "Serbian" })" :)
for $i in json-file("../../queries/conf-ex.json")
let $target := $i."target"
let $guess := $i."guess"
return {"target" : $target, "guess": $guess}
