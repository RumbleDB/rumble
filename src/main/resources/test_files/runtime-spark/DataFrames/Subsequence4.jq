(:JIQS: ShouldRun; Output="" :)
subsequence(annotate((for $i in 1 to 1000 return {"foo" : $i}), { "foo" : "integer" }), 1001, 2)
