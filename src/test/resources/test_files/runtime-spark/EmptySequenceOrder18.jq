(:JIQS: ShouldRun; Output="({ "foo" : [ 3 ] }, { "foo" : [ 2 ] }, { "foo" : [ 1 ] }, { "foo" : [ null ] }, { "foo" : [ ] })" :)

declare default order empty greatest;

declare variable $seq := parallelize(([], [1], [null], [3], [2]));

for $i in $seq
order by $i[] descending empty least
return { "foo" : $i }
