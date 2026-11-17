(:JIQS: ShouldRun; Output="({ "foo" : [ 1 ], "pos" : 1 }, { "foo" : [ 2 ], "pos" : 1 })" :)

declare variable $seq := (
  { "foo" : [1] },
  { "foo" : [2, 3, 4] },
  { "foo" : [] },
  { "foo" : [5] }
);
variable $res := (), $counter := 0;
for $i in parallelize($seq.foo)
for $foo allowing empty at $pos in $i[]
return {
        $res := ($res, { "foo" : [ $foo ], "pos" : $pos });
        if ($counter eq 1) then break loop;
        else $counter := $counter + 1;
        }
$res