(:JIQS: ShouldNotCompile; ErrorCode="SCCP0005"; ErrorMetadata="LINE:13:COLUMN:4:" :)
declare variable $seq := (
  { "foo" : [1] },
  { "foo" : [2, 3, 4] },
  { "foo" : [] },
  { "foo" : [5] }
);
variable $res := ();
for $i in parallelize($seq.foo)
for $foo allowing empty at $pos in $i[]
return {
    $res := ($res, { "foo" : [ $foo ], "pos" : $pos });
    $foo := 3;
}
$res