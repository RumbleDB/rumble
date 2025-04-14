(:JIQS: ShouldRun; Output="({ "target" : "Russian", "count" : 1 }, { "target" : "Russian", "count" : 2 }, { "target" : "Czech", "count" : 3 }, { "target" : "Serbian", "count" : 4 }, { "target" : "Serbian", "count" : 5 })" :)
for $i in json-lines("../../../queries/conf-ex.json", 10)
count $c
return { "target" : $i."target", "count" : $c }
