(:JIQS: ShouldRun; Output="{ "foo" : [ 1 ], "pos" : 1 }" :)

declare variable $seq := (
  { "foo" : [1] },
  { "foo" : [2, 3, 4] },
  { "foo" : [] },
  { "foo" : [5] }
);
variable $res := ();
for $i in parallelize($seq.foo)
for $foo allowing empty at $pos in $i[]
return exit returning {$res := ($res, { "foo" : [ $foo ], "pos" : $pos }); $res};
$res := [1, 2];
$res