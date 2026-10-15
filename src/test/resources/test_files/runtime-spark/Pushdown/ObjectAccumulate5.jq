(:JIQS: ShouldRun; Output="{ }" :)
accumulate(annotate(parallelize(()), { "a" : "integer" }))

(: an accumulation over an empty DataFrame returns an empty object :)
