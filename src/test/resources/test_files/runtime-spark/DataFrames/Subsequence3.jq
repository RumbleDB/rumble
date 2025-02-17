(:JIQS: ShouldRun; Output="({ "foo" : 1 }, { "foo" : 2 })" :)
subsequence(annotate((for $i in 1 to 1000 return {"foo" : $i}), { "foo" : "integer" }), 1, 2)
