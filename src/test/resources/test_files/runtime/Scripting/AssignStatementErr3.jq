(:JIQS: ShouldNotCompile; ErrorCode="SCCP0005"; ErrorMetadata="LINE:11:COLUMN:7:" :)
declare variable $seq := (
  { "foo" : [1] },
  { "foo" : [2, 3, 4] },
  { "foo" : [] },
  { "foo" : [5] }
);
variable $res := ();
for $i in parallelize($seq.foo)
let $a := {"a" : 1}
return $a := {"b" : 2};
$res