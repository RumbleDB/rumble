(:JIQS: ShouldRun; Output="({ "foo" : [ ] }, { "foo" : [ null ] }, { "foo" : [ 1 ] }, { "foo" : [ 2 ] }, { "foo" : [ 3 ] })" :)

declare default order empty least;

declare variable $seq := ([], [1], [null], [3], [2]);
variable $res := ();
for $i in $seq
stable order by $i[] empty least
return $res := ($res, { "foo" : $i });
$res
