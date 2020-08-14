(:JIQS: ShouldRun; Output="({ "foo" : [ ] }, { "foo" : [ null ] }, { "foo" : [ 1 ] }, { "foo" : [ 2 ] }, { "foo" : [ 3 ] })" :)

declare default order empty greatest;

declare variable $seq := parallelize(([], [1], [null], [3], [2]));

for $i in $seq
order by $i[] empty least
return { "foo" : $i }
