(:JIQS: ShouldRun; Output="({ "foo" : 95 }, { "foo" : 96 }, { "foo" : 97 })" :)
subsequence(annotate((for $i in 1 to 100 return {"foo" : $i}), { "foo" : "integer" }), 95, 3)
