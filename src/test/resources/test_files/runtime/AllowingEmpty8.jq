(:JIQS: ShouldRun; Output="({ "foo" : [ 1 ], "pos" : 1 }, { "foo" : [ 2 ], "pos" : 2 }, { "foo" : [ 3 ], "pos" : 3 }, { "foo" : [ 4 ], "pos" : 4 }, { "foo" : [ 5 ], "pos" : 5 })" :)

declare variable $seq := (
  { "foo" : [1] },
  { "foo" : [2, 3, 4] },
  { "foo" : [] },
  { "foo" : [5] }
);

for $foo allowing empty at $pos in $seq.foo[]
return { "foo" : [ $foo ], "pos" : $pos }
