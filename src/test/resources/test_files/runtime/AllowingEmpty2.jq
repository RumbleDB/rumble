(:JIQS: ShouldRun; Output="({ "foo" : [ ] }, { "foo" : [ null ] }, { "foo" : [ 1 ] }, { "foo" : [ 2 ] }, { "foo" : [ 3 ] })" :)

declare variable $seq := (
  { "foo" : [1] },
  { "foo" : [2, 3, 4] },
  { "foo" : [] },
  { "foo" : [5] }
);

for $foo allowing empty in $seq.foo[]
return { "foo" : [ $foo ] }
