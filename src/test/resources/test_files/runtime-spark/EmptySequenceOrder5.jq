(:JIQS: ShouldRun; Output="({ "foo" : [ ] }, { "foo" : [ 3 ] }, { "foo" : [ 2 ] }, { "foo" : [ 1 ] }, { "foo" : [ null ] })" :)

declare variable $seq := parallelize(([], [1], [null], [3], [2]));

for $i in $seq
order by $i[] descending empty greatest
return { "foo" : $i }
