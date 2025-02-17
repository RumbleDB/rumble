(:JIQS: ShouldRun; Output="({ "foo" : [ 1 ], "pos" : 1 }, { "foo" : [ 2 ], "pos" : 1 }, { "foo" : [ 3 ], "pos" : 2 }, { "foo" : [ 4 ], "pos" : 3 }, { "foo" : [ ], "pos" : 0 }, { "foo" : [ 5 ], "pos" : 1 })" :)

declare variable $seq := (
  { "foo" : [1] },
  { "foo" : [2, 3, 4] },
  { "foo" : [] },
  { "foo" : [5] }
);
variable $res := ();
for $i in parallelize($seq.foo)
for $foo allowing empty at $pos in $i[]
return $res := ($res, { "foo" : [ $foo ], "pos" : $pos });
$res