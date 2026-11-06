(:JIQS: ShouldRun; Output="({ "left" : 1, "right" : 1, "$k" : "foo" }, { "left" : 1, "right" : 2, "$k" : "foo" }, { "left" : 1, "right" : 3, "$k" : "foo" }, { "left" : 2, "right" : 1, "$k" : "foo" }, { "left" : 2, "right" : 2, "$k" : "foo" }, { "left" : 2, "right" : 3, "$k" : "foo" }, { "left" : 3, "right" : 1, "$k" : "foo" }, { "left" : 3, "right" : 2, "$k" : "foo" }, { "left" : 3, "right" : 3, "$k" : "foo" })" :)
let $k := "foo"
return
for $i in parallelize((1,2,3))
for $j in parallelize((1,2,3))
order by $i, $j
return { left: $i, right: $j, "$k": $k }

(: Cartesian product with two Spark-enabled for clauses with an outer flwor clause :)
