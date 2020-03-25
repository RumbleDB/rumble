(:JIQS: ShouldRun; Output="({ "foo" : 999 }, { "foo" : 1000 })" :)
subsequence(annotate((for $i in 1 to 1000 return {"foo" : $i}), { "foo" : "integer" }), 999, 3)
