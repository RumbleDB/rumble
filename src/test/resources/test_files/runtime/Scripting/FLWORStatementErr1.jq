(:JIQS: ShouldCrash; ErrorCode="XQST0049" :)
declare variable $seq := (
  { "foo" : [1] },
  { "foo" : [2, 3, 4] },
  { "foo" : [] },
  { "foo" : [5] }
);
for $i in parallelize($seq.foo)
for $foo allowing empty at $pos in $i[]
return {
        variable $foo := 10;
        variable $pos := 20;
        $foo + $pos;
}