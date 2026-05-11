(:JIQS: ShouldRun; Output="[ { "foo" : [ ] }, { "foo" : [ 3 ] }, { "foo" : [ 2 ] }, { "foo" : [ 1 ] }, { "foo" : [ null ] } ]" :)

declare default order empty greatest;

declare variable $seq := ([], [1], [null], [3], [2]);
variable $res := [], $counter := 1;
for $i in $seq
order by $i[] descending empty greatest
return {
    insert json { "foo" : $i } into $res at position $counter;
    $counter := $counter + 1;
}
$res
