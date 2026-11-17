(:JIQS: ShouldRun; Output="({ "foo" : [ 1 ] }, { "foo" : [ 2 ] }, { "foo" : [ 3 ] }, { "foo" : [ 4 ] }, { "foo" : [ ] }, { "foo" : [ 5 ] })" :)

declare variable $seq := (
  { "foo" : [1] },
  { "foo" : [2, 3, 4] },
  { "foo" : [] },
  { "foo" : [5] }
);
variable $res := ();
for $i in $seq.foo
for $foo allowing empty in $i[]
return $res := ($res, { "foo" : [ $foo ] });
$res