(:JIQS: ShouldRun; Output="{ "a" : [ 1, 3 ], "b" : 2 }" :)
accumulate(annotate(parallelize(({ "a" : 1, "b" : 2 }, { "a" : 3 })), { "a" : "integer", "b" : "integer" }))

(: an accumulation over a DataFrame keeps keys that only some objects have :)
