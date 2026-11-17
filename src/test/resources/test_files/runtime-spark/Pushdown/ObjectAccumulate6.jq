(:JIQS: ShouldRun; Output="{ "a" : [ 1, 2, 3, 4, 5, 6, 7, 8 ] }" :)
accumulate(annotate(parallelize(for $i in 1 to 8 return { "a" : $i }, 4), { "a" : "integer" }))

(: an accumulation over a DataFrame keeps the values in input order across partitions :)
