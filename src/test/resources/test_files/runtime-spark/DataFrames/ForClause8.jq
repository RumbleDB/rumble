(:JIQS: ShouldRun; Output="({ "left" : 1, "right" : 1 }, { "left" : 1, "right" : 2 }, { "left" : 1, "right" : 3 }, { "left" : 2, "right" : 1 }, { "left" : 2, "right" : 2 }, { "left" : 2, "right" : 3 }, { "left" : 3, "right" : 1 }, { "left" : 3, "right" : 2 }, { "left" : 3, "right" : 3 })" :)
for $i in parallelize((1,2,3))
for $j in parallelize((1,2,3))
order by $i, $j
return { left: $i, right: $j }

(: Cartesian product with two Spark-enabled for clauses :)
